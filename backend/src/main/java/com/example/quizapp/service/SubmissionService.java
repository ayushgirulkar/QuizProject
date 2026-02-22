package com.example.quizapp.service;

import com.example.quizapp.exception.ApiException;
import com.example.quizapp.model.*;
import com.example.quizapp.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class SubmissionService {
    private final SubmissionRepository subRepo;
    private final SubmissionAnswerRepository ansRepo;
    private final QuizRepository quizRepo;
    private final UserRepository userRepo;
    private final QuestionRepository questionRepo;
    private final OptionRepository optionRepo;

    public SubmissionService(SubmissionRepository subRepo, SubmissionAnswerRepository ansRepo,
                             QuizRepository quizRepo, UserRepository userRepo,
                             QuestionRepository questionRepo, OptionRepository optionRepo) {
        this.subRepo = subRepo;
        this.ansRepo = ansRepo;
        this.quizRepo = quizRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.optionRepo = optionRepo;
    }

    @Transactional
    public Submission submit(Long quizId, Long userId, Instant startedAt, Map<Long, Long> answers) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ApiException("Quiz not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        if (quiz.getValidFrom() != null && now.isBefore(quiz.getValidFrom())) {
            throw new ApiException("Quiz not yet open");
        }

        if (quiz.getValidUntil() != null && now.isAfter(quiz.getValidUntil())) {
            throw new ApiException("Quiz closed");
        }

        subRepo.findByQuizAndUser(quiz, user).ifPresent(s -> {
            throw new ApiException("You have already submitted this quiz");
        });

        Instant mustSubmitBy = startedAt.plusSeconds(quiz.getTimeLimitMinutes() * 60L);

        Submission sub = new Submission();
        sub.setQuiz(quiz);
        sub.setUser(user);
        sub.setStartedAt(startedAt);
        sub.setSubmittedAt(Instant.now());

        int score = 0;
        for (Question q : quiz.getQuestions()) {
            Long selOptionId = answers.get(q.getId());
            if (selOptionId == null) continue;
            OptionChoice opt = optionRepo.findById(selOptionId)
                    .orElseThrow(() -> new ApiException("Invalid option selected"));
            if (opt.isCorrect()) score++;

            SubmissionAnswer sa = new SubmissionAnswer();
            sa.setSubmission(sub);
            sa.setQuestion(q);
            sa.setSelectedOption(opt);
        }

        int total = quiz.getQuestions().size();
        int timeTaken = (int) Math.min(
                Duration.between(startedAt, Instant.now()).toSeconds(),
                quiz.getTimeLimitMinutes() * 60L
        );

        sub.setScore(score);
        sub.setTimeTakenSeconds(timeTaken);

        Submission saved = subRepo.save(sub);

        // Save each answer after submission is saved
        for (Question q : quiz.getQuestions()) {
            Long selOptionId = answers.get(q.getId());
            if (selOptionId == null) continue;
            OptionChoice opt = optionRepo.findById(selOptionId)
                    .orElseThrow(() -> new ApiException("Invalid option selected"));
            SubmissionAnswer sa = new SubmissionAnswer();
            sa.setSubmission(saved);
            sa.setQuestion(q);
            sa.setSelectedOption(opt);
            ansRepo.save(sa);
        }

        return saved;
    }
}
