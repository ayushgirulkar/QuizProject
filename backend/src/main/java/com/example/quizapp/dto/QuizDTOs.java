package com.example.quizapp.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

public class QuizDTOs {
    public static class GenerateQuizRequest {
        @NotNull public Long adminId;
        @NotBlank public String title;
        @NotBlank public String pasteText;
        @Min(1) @Max(50) public int numQuestions;
        @Min(1) @Max(180) public int timeLimitMinutes;
        @NotNull public LocalDateTime validFrom;
        @NotNull public LocalDateTime validUntil;
    }

    public static class QuizSummary {
        public Long id; public String title; public String code;
        public int timeLimitMinutes; public LocalDateTime validFrom; public LocalDateTime validUntil;
        public QuizSummary(Long id,String title,String code,int t,LocalDateTime vf,LocalDateTime vu){
            this.id=id; this.title=title; this.code=code; this.timeLimitMinutes=t; this.validFrom=vf; this.validUntil=vu;
        }
    }

    public static class OptionView { public Long id; public String text; public OptionView(Long id,String text){this.id=id;this.text=text;} }
    public static class QuestionView { public Long id; public String text; public List<OptionView> options; }
    public static class QuizAttemptView {
        public Long id; public String title; public String code; public String adminName;
        public int timeLimitMinutes; public LocalDateTime validFrom; public LocalDateTime validUntil;
        public List<QuestionView> questions;
    }

    public static class AdminQuestionView extends QuestionView { public Long correctOptionId; }
    public static class AdminQuizView extends QuizAttemptView { public List<AdminQuestionView> questions; }
}
