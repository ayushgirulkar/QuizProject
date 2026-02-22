package com.example.quizapp.model;

import jakarta.persistence.*;
import java.util.*;

@Entity @Table(name="questions")
public class Question {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Quiz quiz;
    @Column(length=2000) private String text;

    @OneToMany(mappedBy="question", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OptionChoice> options = new ArrayList<>();

    public Long getId() { return id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<OptionChoice> getOptions() { return options; }
}
