package com.mizilin.firstbot.repository;

import com.mizilin.firstbot.entity.Quiz;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @EntityGraph(attributePaths = {"questions", "options", "results"})
    Quiz findWithQuestionsAndOptionsAndResultsById(Long id);
}
