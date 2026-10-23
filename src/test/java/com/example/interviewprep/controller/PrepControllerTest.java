package com.example.interviewprep.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrepController.class)
class PrepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPrepStartDefault() throws Exception {
        mockMvc.perform(get("/api/v1/prepStart")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.topic").value("Java & Spring Boot Microservices"))
                .andExpect(jsonPath("$.sessionId").isNotEmpty())
                .andExpect(jsonPath("$.modules").isArray());
    }

    @Test
    void testPrepStartCustomTopic() throws Exception {
        mockMvc.perform(get("/prepStart")
                        .param("topic", "System Design")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.topic").value("System Design"))
                .andExpect(jsonPath("$.sessionId").isNotEmpty());
    }
}
