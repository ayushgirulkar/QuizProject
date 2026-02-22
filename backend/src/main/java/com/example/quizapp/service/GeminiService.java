package com.example.quizapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    private final RestTemplate http = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public static class GenQ {
        public String question;
        public List<String> options;
        public int answerIndex;
    }

    private static final String QUIZ_JSON_SCHEMA = """
        {
          "type": "object",
          "properties": {
            "questions": {
              "type": "array",
              "items": {
                "type": "object",
                "properties": {
                  "question": {"type": "string"},
                  "options": {
                    "type": "array",
                    "items": {"type": "string"},
                    "minItems": 4,
                    "maxItems": 4
                  },
                  "answerIndex": {
                    "type": "integer",
                    "minimum": 0,
                    "maximum": 3
                  }
                },
                "required": ["question", "options", "answerIndex"]
              }
            }
          },
          "required": ["questions"]
        }
    """;

    public List<GenQ> generate(String sourceText, int numQuestions) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("GEMINI_API_KEY not set");
        }

        // ✅ Correct endpoint for Gemini 2.5
        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model + ":generateContent?key=" + apiKey;

        String promptText = """
Based on the text below, generate %d multiple-choice questions directly from the content. 
Each question must:
- Be numbered as Q1, Q2, etc.
- Use exact facts from the text.
- Have exactly 4 options, one correct answer.
- Return as JSON following this schema.

Text:
%s
""".formatted(numQuestions, sourceText);



        // ✅ Request body structure as per Gemini v1beta spec
        ObjectNode body = mapper.createObjectNode();

        ArrayNode contents = mapper.createArrayNode();
        ObjectNode content = mapper.createObjectNode();
        ArrayNode parts = mapper.createArrayNode();
        parts.addObject().put("text", promptText);
        content.set("parts", parts);
        contents.add(content);
        body.set("contents", contents);

        ObjectNode generationConfig = mapper.createObjectNode();
        generationConfig.put("temperature", 0.0);
        generationConfig.put("response_mime_type", "application/json");
        try {
            generationConfig.set("response_schema", mapper.readTree(QUIZ_JSON_SCHEMA));
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON schema", e);
        }
        body.set("generationConfig", generationConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> resp;
        try {
            resp = http.postForEntity(endpoint, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Gemini API: " + e.getMessage());
        }

        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API error: " + resp.getStatusCode() + " " + resp.getBody());
        }

        try {
            JsonNode root = mapper.readTree(resp.getBody());
            JsonNode candidates = root.path("candidates");
            if (candidates.isEmpty()) {
                throw new RuntimeException("Gemini returned no candidates: " + resp.getBody());
            }

            String jsonOutput = candidates.get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();

            List<GenQ> out = new ArrayList<>();
            JsonNode questions = mapper.readTree(jsonOutput).path("questions");
            for (JsonNode qn : questions) {
                GenQ q = new GenQ();
                q.question = qn.path("question").asText();
                q.options = new ArrayList<>();
                for (JsonNode opt : qn.path("options")) q.options.add(opt.asText());
                q.answerIndex = qn.path("answerIndex").asInt();
                out.add(q);
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }
}
