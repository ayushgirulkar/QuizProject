package com.example.quizapp.repo;

import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.Submission;
import com.example.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUserId(Long userId);       // for history
    List<Submission> findByQuiz(Quiz quiz);           // all submissions of a quiz

    Optional<Submission> findByQuizAndUser(Quiz quiz, User user); // 👈 add this
}
