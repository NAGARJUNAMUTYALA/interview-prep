package com.example.interviewprep.controller;

import com.example.interviewprep.dto.PrepStartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class PrepController {

    @GetMapping({"/api/v1/prepStart", "/prepStart"})
    public ResponseEntity<PrepStartResponse> startPrep(
            @RequestParam(name = "topic", defaultValue = "Java & Spring Boot Microservices") String topic
    ) {
        PrepStartResponse response = new PrepStartResponse(
                "SUCCESS",
                "Interview preparation session initialized successfully",
                UUID.randomUUID().toString(),
                topic,
                Instant.now(),
                List.of(
                        "Core Java Fundamentals & Memory Model",
                        "Spring Boot & Microservices Architecture",
                        "Concurrency & Multithreading",
                        "Database Optimization & Distributed Caching",
                        "System Design & REST API Best Practices"
                )
        );
        return ResponseEntity.ok(response);
    }
}
