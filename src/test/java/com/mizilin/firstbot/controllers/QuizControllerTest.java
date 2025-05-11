package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.dto.QuizDto;
import com.mizilin.firstbot.entity.Quiz;
import com.mizilin.firstbot.service.QuizService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizService quizService;


    @Test
    void listQuizzesTest() throws Exception {
        List<Quiz> quizzes = Arrays.asList(new Quiz(), new Quiz());
        when(quizService.getAllQuizzes()).thenReturn(quizzes);

        mockMvc.perform(get("/quiz/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-list"))
                .andExpect(model().attribute("quizzes", quizzes));

        verify(quizService).getAllQuizzes();
    }

    @Test
    void deleteQuizTest() throws Exception {
        Long quizId = 1L;

        mockMvc.perform(post("/quiz/delete/{id}", quizId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/list?success"));

        verify(quizService).deleteQuiz(quizId);
    }

    @Test
    void showQuizFormTest() throws Exception {
        mockMvc.perform(get("/quiz//create"))
                .andExpect(status().isOk())
                .andExpect(view().name("addQuiz"));
    }

    @Test
    void saveQuizTest() throws Exception {
        String quizTitle = "Sample Quiz";
        String quizDescription = "Description here";

        mockMvc.perform(post("/quiz/addQuiz")
                        .param("title", quizTitle)
                        .param("description", quizDescription))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/create?success"));

        verify(quizService).saveQuiz(any(QuizDto.class));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        QuizService quizService() {
            return Mockito.mock(QuizService.class);
        }
    }
}
