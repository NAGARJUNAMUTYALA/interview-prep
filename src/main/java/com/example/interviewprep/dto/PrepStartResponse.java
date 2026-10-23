package com.example.interviewprep.dto;

import java.time.Instant;
import java.util.List;

public record PrepStartResponse(
        String status,
        String message,
        String sessionId,
        String topic,
        Instant startedAt,
        List<String> modules
) {}
