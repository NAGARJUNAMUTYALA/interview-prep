package com.example.interviewprep.controller;

import com.example.interviewprep.dto.PrepStartResponse;
import com.example.interviewprep.service.PrepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrepController {

    private final PrepService prepService;

    public PrepController(PrepService prepService) {
        this.prepService = prepService;
    }

    @GetMapping({"/api/v1/prepStart", "/prepStart"})
    public ResponseEntity<PrepStartResponse> startPrep(
            @RequestParam(name = "topic", defaultValue = "Java & Spring Boot Microservices") String topic
    ) {
        PrepStartResponse response = prepService.startPreparation(topic);
        return ResponseEntity.ok(response);
    }
}
