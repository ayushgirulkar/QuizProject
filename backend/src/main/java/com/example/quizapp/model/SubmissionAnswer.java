package com.example.quizapp.model;

import jakarta.persistence.*;

@Entity @Table(name="submission_answers")
public class SubmissionAnswer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;

    @ManyToOne(optional=false) private Submission submission;
    @ManyToOne(optional=false) private Question question;
    @ManyToOne(optional=false) private OptionChoice selectedOption;

    public Long getId() { return id; }
    public Submission getSubmission() { return submission; }
    public void setSubmission(Submission submission) { this.submission = submission; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public OptionChoice getSelectedOption() { return selectedOption; }
    public void setSelectedOption(OptionChoice selectedOption) { this.selectedOption = selectedOption; }
}
