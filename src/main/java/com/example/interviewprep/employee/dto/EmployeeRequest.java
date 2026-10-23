package com.example.interviewprep.employee.dto;

import com.example.interviewprep.employee.entity.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Immutable request DTO for creating or updating an employee.
 * Enforces declarative Jakarta Bean Validation rules.
 */
public record EmployeeRequest(
        @NotBlank(message = "First name is mandatory")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @NotBlank(message = "Last name is mandatory")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @NotNull(message = "Department is mandatory")
        Department department,

        @NotNull(message = "Salary is mandatory")
        @PositiveOrZero(message = "Salary must be greater than or equal to zero")
        BigDecimal salary
) {}
