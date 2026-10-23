package com.example.interviewprep.employee.controller;

import com.example.interviewprep.common.exception.DuplicateResourceException;
import com.example.interviewprep.common.exception.GlobalExceptionHandler;
import com.example.interviewprep.common.exception.ResourceNotFoundException;
import com.example.interviewprep.employee.dto.EmployeeRequest;
import com.example.interviewprep.employee.dto.EmployeeResponse;
import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private final EmployeeResponse sampleResponse = new EmployeeResponse(
            1L,
            "Asha",
            "Rao",
            "Asha Rao",
            "asha.rao@example.com",
            Department.ENGINEERING,
            new BigDecimal("85000.00"),
            Instant.now(),
            Instant.now()
    );

    @Test
    void testCreateEmployee_Success() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Asha", "Rao", "asha.rao@example.com", Department.ENGINEERING, new BigDecimal("85000.00"));
        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/employees/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Asha Rao"))
                .andExpect(jsonPath("$.email").value("asha.rao@example.com"));
    }

    @Test
    void testCreateEmployee_ValidationFailure_ReturnsBadRequest() throws Exception {
        // Missing firstName, invalid email, negative salary
        EmployeeRequest invalidRequest = new EmployeeRequest("", "Rao", "not-an-email", Department.ENGINEERING, new BigDecimal("-10.00"));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.firstName").isNotEmpty())
                .andExpect(jsonPath("$.validationErrors.email").isNotEmpty())
                .andExpect(jsonPath("$.validationErrors.salary").isNotEmpty());
    }

    @Test
    void testCreateEmployee_DuplicateEmail_ReturnsConflict() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Asha", "Rao", "duplicate@example.com", Department.ENGINEERING, new BigDecimal("85000.00"));
        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenThrow(new DuplicateResourceException("An employee with email 'duplicate@example.com' already exists"));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("An employee with email 'duplicate@example.com' already exists"));
    }

    @Test
    void testGetEmployeeById_Success() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("asha.rao@example.com"));
    }

    @Test
    void testGetEmployeeById_NotFound_ReturnsNotFound() throws Exception {
        when(employeeService.getEmployeeById(99L))
                .thenThrow(new ResourceNotFoundException("Employee with id 99 not found"));

        mockMvc.perform(get("/api/v1/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Employee with id 99 not found"));
    }

    @Test
    void testGetAllEmployees_Success() throws Exception {
        when(employeeService.getAllEmployees(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleResponse)));

        mockMvc.perform(get("/api/v1/employees")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testUpdateEmployee_Success() throws Exception {
        EmployeeRequest updateRequest = new EmployeeRequest("Asha", "Rao", "asha.rao@example.com", Department.FINANCE, new BigDecimal("90000.00"));
        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteEmployee_Success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());
    }
}
