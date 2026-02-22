package com.example.quizapp.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;

public class SubmitDTOs {
    public static class Answer {
        @NotNull public Long questionId;
        @NotNull public Long optionId;
    }
    public static class SubmitRequest {
        @NotNull public Long quizId;
        @NotNull public Long userId;
        @NotNull public Instant startedAt;
        @NotEmpty public List<Answer> answers;
    }
    public static class SubmitResponse {
        public int score; public int total; public int timeTakenSeconds;
        public SubmitResponse(int score,int total,int time){ this.score=score; this.total=total; this.timeTakenSeconds=time; }
    }

    public static class ResultRow {
        public String quizTitle; public String studentName; public int score; public int timeTakenSeconds; public Instant submittedAt;
        public ResultRow(String qt,String sn,int sc,int t,Instant s){ quizTitle=qt; studentName=sn; score=sc; timeTakenSeconds=t; submittedAt=s; }
    }
}
