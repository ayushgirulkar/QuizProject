package com.example.quizapp.controller;

import com.example.quizapp.dto.SubmitDTOs.*;
import com.example.quizapp.model.*;
import com.example.quizapp.repo.*;
import com.example.quizapp.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService subService;
    private final QuizRepository quizRepo;
    private final SubmissionRepository subRepo;

    public SubmissionController(SubmissionService subService, QuizRepository quizRepo, SubmissionRepository subRepo) {
        this.subService = subService;
        this.quizRepo = quizRepo;
        this.subRepo = subRepo;
    }

    @PostMapping("/submit")
    public SubmitResponse submit(@Valid @RequestBody SubmitRequest req) {
        Map<Long, Long> ans = new HashMap<>();
        for (Answer a : req.answers) {
            ans.put(a.questionId, a.optionId);
        }

        Submission sub = subService.submit(req.quizId, req.userId, req.startedAt, ans);
        int total = quizRepo.findById(req.quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"))
                .getQuestions()
                .size();

        return new SubmitResponse(sub.getScore(), total, sub.getTimeTakenSeconds());
    }

    @GetMapping("/results/{quizId}")
    public List<ResultRow> results(@PathVariable Long quizId) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        return subRepo.findByQuiz(quiz).stream()
                .map(s -> new ResultRow(
                        quiz.getTitle(),
                        s.getUser().getName(),
                        s.getScore(),
                        s.getTimeTakenSeconds(),
                        s.getSubmittedAt()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/history/{userId}")
    public List<ResultRow> history(@PathVariable Long userId) {
        List<Submission> subs = subRepo.findByUserId(userId);
        return subs.stream()
                .map(s -> new ResultRow(
                        s.getQuiz().getTitle(),
                        s.getUser().getName(),
                        s.getScore(),
                        s.getTimeTakenSeconds(),
                        s.getSubmittedAt()
                ))
                .collect(Collectors.toList());
    }
}
