package com.mizilin.firstbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


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
    @JoinColumn(name = "quiz_id")
    @JsonBackReference("quiz-options")
    private Quiz quiz;

}
