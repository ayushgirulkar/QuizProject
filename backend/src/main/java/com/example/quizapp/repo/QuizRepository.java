package com.example.quizapp.repo;

import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    Optional<Quiz> findByCode(String code);
    List<Quiz> findByCreatedBy(User user);
}
