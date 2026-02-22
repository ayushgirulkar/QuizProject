package com.example.quizapp.repo;

import com.example.quizapp.model.OptionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<OptionChoice,Long> {}
