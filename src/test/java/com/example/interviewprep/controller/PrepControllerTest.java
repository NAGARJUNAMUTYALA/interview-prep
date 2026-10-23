package com.example.interviewprep.controller;

import com.example.interviewprep.dto.PrepStartResponse;
import com.example.interviewprep.service.PrepService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrepController.class)
class PrepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrepService prepService;

    @Test
    void testPrepStartDefault() throws Exception {
        PrepStartResponse mockResponse = new PrepStartResponse(
                "SUCCESS",
                "Interview preparation session initialized successfully",
                UUID.randomUUID().toString(),
                "Java & Spring Boot Microservices",
                Instant.now(),
                List.of("Core Java", "Spring Boot")
        );
        when(prepService.startPreparation("Java & Spring Boot Microservices")).thenReturn(mockResponse);

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
        PrepStartResponse mockResponse = new PrepStartResponse(
                "SUCCESS",
                "Interview preparation session initialized successfully",
                UUID.randomUUID().toString(),
                "System Design",
                Instant.now(),
                List.of("Microservices", "Scalability")
        );
        when(prepService.startPreparation("System Design")).thenReturn(mockResponse);

        mockMvc.perform(get("/prepStart")
                        .param("topic", "System Design")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.topic").value("System Design"))
                .andExpect(jsonPath("$.sessionId").isNotEmpty());
    }
}
