package com.example.interviewprep.employee.service;

import com.example.interviewprep.common.exception.DuplicateResourceException;
import com.example.interviewprep.common.exception.ResourceNotFoundException;
import com.example.interviewprep.employee.dto.EmployeeRequest;
import com.example.interviewprep.employee.dto.EmployeeResponse;
import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.entity.Employee;
import com.example.interviewprep.employee.repository.EmployeeRepository;
import com.example.interviewprep.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sampleEmployee;
    private EmployeeRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee("Asha", "Rao", "asha.rao@example.com", Department.ENGINEERING, new BigDecimal("85000.00"));
        sampleEmployee.setId(1L);

        sampleRequest = new EmployeeRequest("Asha", "Rao", "asha.rao@example.com", Department.ENGINEERING, new BigDecimal("85000.00"));
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepository.existsByEmail("asha.rao@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        EmployeeResponse response = employeeService.createEmployee(sampleRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Asha Rao", response.fullName());
        assertEquals("asha.rao@example.com", response.email());
        assertEquals(Department.ENGINEERING, response.department());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_DuplicateEmail_ThrowsDuplicateResourceException() {
        when(employeeRepository.existsByEmail("asha.rao@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(sampleRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Asha Rao", response.fullName());
    }

    @Test
    void getEmployeeById_NotFound_ThrowsResourceNotFoundException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(99L));
    }

    @Test
    void getAllEmployees_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(sampleEmployee));
        when(employeeRepository.searchEmployees(Department.ENGINEERING, "Asha", pageable)).thenReturn(page);

        Page<EmployeeResponse> result = employeeService.getAllEmployees(Department.ENGINEERING, "Asha", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Asha Rao", result.getContent().get(0).fullName());
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        EmployeeRequest updateRequest = new EmployeeRequest("Asha", "Kumar", "asha.rao@example.com", Department.PRODUCT, new BigDecimal("95000.00"));
        EmployeeResponse response = employeeService.updateEmployee(1L, updateRequest);

        assertNotNull(response);
        verify(employeeRepository, times(1)).save(sampleEmployee);
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_NotFound_ThrowsResourceNotFoundException() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(99L));
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
