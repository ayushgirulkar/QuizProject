package com.example.quizapp.service;

import com.example.quizapp.model.*;
import com.example.quizapp.repo.*;
import com.example.quizapp.util.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {
    private final QuizRepository quizRepo;
    private final UserRepository userRepo;
    private final GeminiService gemini;

    public QuizService(QuizRepository quizRepo, UserRepository userRepo, GeminiService gemini) {
        this.quizRepo = quizRepo; this.userRepo = userRepo; this.gemini = gemini;
    }

    @Transactional
    public Quiz createFromGemini(Long adminId, String title, String pasteText, int numQuestions,
                                 int timeLimitMinutes, LocalDateTime validFrom, LocalDateTime validUntil) {
        User admin = userRepo.findById(adminId).orElseThrow();
        String code;
        do { code = CodeGenerator.code(6); } while (quizRepo.findByCode(code).isPresent());

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(pasteText);
        quiz.setCode(code);
        quiz.setTimeLimitMinutes(timeLimitMinutes);
        quiz.setValidFrom(validFrom);
        quiz.setValidUntil(validUntil);
        quiz.setCreatedBy(admin);

        var generated = gemini.generate(pasteText, numQuestions);
        for (var gq : generated) {
            Question q = new Question();
            q.setQuiz(quiz);
            q.setText(gq.question);
            int idx=0;
            for (String opt : gq.options) {
                OptionChoice oc = new OptionChoice();
                oc.setQuestion(q);
                oc.setText(opt);
                oc.setCorrect(idx==gq.answerIndex);
                q.getOptions().add(oc);
                idx++;
            }
            quiz.getQuestions().add(q);
        }
        return quizRepo.save(quiz);
    }
}
