package com.example.interviewprep.employee.dto;

import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.entity.Employee;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Immutable response DTO representing an Employee resource returned by the API.
 */
public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        Department department,
        BigDecimal salary,
        Instant createdAt,
        Instant updatedAt
) {
    public static EmployeeResponse fromEntity(Employee entity) {
        return new EmployeeResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getFirstName() + " " + entity.getLastName(),
                entity.getEmail(),
                entity.getDepartment(),
                entity.getSalary(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
