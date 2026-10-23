package com.example.interviewprep.service.impl;

import com.example.interviewprep.dto.PrepStartResponse;
import com.example.interviewprep.service.PrepService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PrepServiceImpl implements PrepService {

    @Override
    public PrepStartResponse startPreparation(String topic) {
        String effectiveTopic = (topic == null || topic.isBlank())
                ? "Java & Spring Boot Microservices"
                : topic.trim();

        return new PrepStartResponse(
                "SUCCESS",
                "Interview preparation session initialized successfully",
                UUID.randomUUID().toString(),
                effectiveTopic,
                Instant.now(),
                List.of(
                        "Core Java Fundamentals & Memory Model",
                        "Spring Boot & Microservices Architecture",
                        "Concurrency & Multithreading",
                        "Database Optimization & Distributed Caching",
                        "System Design & REST API Best Practices"
                )
        );
    }
}
