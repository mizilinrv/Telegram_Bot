package com.mizilin.firstbot.controllers;

import com.mizilin.firstbot.QuizBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BroadcastController.class)
//@Import(BroadcastControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class BroadcastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizBot quizBot;

    @Test
    void showNewPostFormTest() throws Exception {
        mockMvc.perform(get("/post"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"));
    }

    @Test
    void sendBroadcastMessageTest() throws Exception {
        String testMessage = "Hello, subscribers!";

        mockMvc.perform(post("/sendBroadcastMessage")
                        .param("message", testMessage))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post?success"));

        verify(quizBot).sendBroadcastMessage(testMessage);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        QuizBot quizBot() {
            return Mockito.mock(QuizBot.class);
        }
    }
}