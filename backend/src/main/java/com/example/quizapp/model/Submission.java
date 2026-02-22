package com.example.quizapp.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="submissions",
        uniqueConstraints=@UniqueConstraint(columnNames={"quiz_id","user_id"}))
public class Submission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="quiz_id") private Quiz quiz;
    @ManyToOne(optional=false) @JoinColumn(name="user_id") private User user;

    private Integer score;
    private Integer timeTakenSeconds;
    private Instant startedAt;
    private Instant submittedAt;

    public Long getId() { return id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(Integer t) { this.timeTakenSeconds = t; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
}
