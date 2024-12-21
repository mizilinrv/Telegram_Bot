package com.mizilin.firstbot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(schema = "quiz_schema", name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private int points;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

}
