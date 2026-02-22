package com.example.quizapp.model;

import jakarta.persistence.*;

@Entity @Table(name="options")
public class OptionChoice {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Question question;
    @Column(length=1000) private String text;
    @Column(nullable=false) private boolean correct;

    public Long getId() { return id; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
