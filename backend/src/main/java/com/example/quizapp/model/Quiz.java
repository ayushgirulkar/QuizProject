package com.example.quizapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="quizzes", indexes = @Index(columnList="code", unique=true))
public class Quiz {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;

    private String title;
    @Column(length=4000) private String description; // admin paste
    @Column(nullable=false, unique=true) private String code; // join code
    @Column(nullable=false) private Integer timeLimitMinutes;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    @ManyToOne(optional=false) private User createdBy;

    @OneToMany(mappedBy="quiz", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Question> questions = new ArrayList<>();

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public List<Question> getQuestions() { return questions; }
}
