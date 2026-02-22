package com.example.quizapp.controller;

import com.example.quizapp.dto.QuizDTOs.*;
import com.example.quizapp.model.*;
import com.example.quizapp.repo.*;
import com.example.quizapp.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final QuizRepository quizRepo;
    private final UserRepository userRepo;

    public QuizController(QuizService quizService, QuizRepository quizRepo, UserRepository userRepo) {
        this.quizService=quizService; this.quizRepo=quizRepo; this.userRepo=userRepo;
    }

    @PostMapping("/generate")
    public AdminQuizView generate(@Valid @RequestBody GenerateQuizRequest req) {
        Quiz quiz = quizService.createFromGemini(req.adminId, req.title, req.pasteText, req.numQuestions,
                req.timeLimitMinutes, req.validFrom, req.validUntil);
        return toAdminView(quiz);
    }

    @GetMapping("/by-code/{code}")
    public QuizAttemptView byCode(@PathVariable String code) {
        Quiz quiz = quizRepo.findByCode(code).orElseThrow();
        return toAttemptView(quiz);
    }

    @GetMapping("/mine/{adminId}")
    public List<QuizSummary> mine(@PathVariable Long adminId) {
        User admin = userRepo.findById(adminId).orElseThrow();
        return quizRepo.findByCreatedBy(admin).stream()
                .map(q -> new QuizSummary(q.getId(), q.getTitle(), q.getCode(),
                        q.getTimeLimitMinutes(), q.getValidFrom(), q.getValidUntil()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{quizId}/admin-view")
    public AdminQuizView adminView(@PathVariable Long quizId) {
        return toAdminView(quizRepo.findById(quizId).orElseThrow());
    }

    private QuizAttemptView toAttemptView(Quiz quiz) {
        QuizAttemptView v = new QuizAttemptView();
        v.id = quiz.getId(); v.title=quiz.getTitle(); v.code=quiz.getCode();
        v.adminName = quiz.getCreatedBy().getName();
        v.timeLimitMinutes = quiz.getTimeLimitMinutes();
        v.validFrom = quiz.getValidFrom(); v.validUntil=quiz.getValidUntil();
        v.questions = new ArrayList<>();
        for (Question q : quiz.getQuestions()) {
            QuestionView qv = new QuestionView();
            qv.id = q.getId(); qv.text = q.getText();
            qv.options = q.getOptions().stream()
                    .map(o -> new OptionView(o.getId(), o.getText())).collect(Collectors.toList());
            v.questions.add(qv);
        }
        return v;
    }

    private AdminQuizView toAdminView(Quiz quiz) {
        AdminQuizView v = new AdminQuizView();
        v.id = quiz.getId(); v.title=quiz.getTitle(); v.code=quiz.getCode();
        v.adminName = quiz.getCreatedBy().getName();
        v.timeLimitMinutes = quiz.getTimeLimitMinutes();
        v.validFrom = quiz.getValidFrom(); v.validUntil=quiz.getValidUntil();
        v.questions = new ArrayList<>();
        for (Question q : quiz.getQuestions()) {
            AdminQuestionView qv = new AdminQuestionView();
            qv.id = q.getId(); qv.text = q.getText();
            qv.options = q.getOptions().stream()
                    .map(o -> new OptionView(o.getId(), o.getText())).collect(Collectors.toList());
            q.getOptions().stream().filter(OptionChoice::isCorrect).findFirst().ifPresent(opt -> qv.correctOptionId = opt.getId());
            v.questions.add(qv);
        }
        return v;
    }
}
