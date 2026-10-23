package com.example.interviewprep.service;

import com.example.interviewprep.dto.PrepStartResponse;
import com.example.interviewprep.service.impl.PrepServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrepServiceTest {

    private PrepService prepService;

    @BeforeEach
    void setUp() {
        prepService = new PrepServiceImpl();
    }

    @Test
    void testStartPreparationWithCustomTopic() {
        PrepStartResponse response = prepService.startPreparation("Spring Cloud & Microservices");

        assertNotNull(response);
        assertEquals("SUCCESS", response.status());
        assertEquals("Spring Cloud & Microservices", response.topic());
        assertNotNull(response.sessionId());
        assertFalse(response.modules().isEmpty());
    }

    @Test
    void testStartPreparationWithNullOrBlankTopic() {
        PrepStartResponse response = prepService.startPreparation(null);

        assertNotNull(response);
        assertEquals("SUCCESS", response.status());
        assertEquals("Java & Spring Boot Microservices", response.topic());
    }
}
