package com.example.interviewprep.employee.service;

import com.example.interviewprep.employee.dto.EmployeeRequest;
import com.example.interviewprep.employee.dto.EmployeeResponse;
import com.example.interviewprep.employee.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * Service contract defining business operations for Employee domain management.
 */
public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    Page<EmployeeResponse> getAllEmployees(Department department, String search, Pageable pageable);

    Page<EmployeeResponse> getAllEmployees(
            Department department,
            String search,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            Pageable pageable
    );

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);
}
