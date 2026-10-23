package com.example.interviewprep.employee.repository;

import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee("Asha", "Rao", "asha.rao@example.com", Department.ENGINEERING, new BigDecimal("85000.00"));
        employee2 = new Employee("Vikram", "Sharma", "vikram.s@example.com", Department.FINANCE, new BigDecimal("75000.00"));

        entityManager.persist(employee1);
        entityManager.persist(employee2);
        entityManager.flush();
    }

    @Test
    void testFindByEmail() {
        Optional<Employee> found = employeeRepository.findByEmail("asha.rao@example.com");
        assertTrue(found.isPresent());
        assertEquals("Asha", found.get().getFirstName());
    }

    @Test
    void testExistsByEmail() {
        assertTrue(employeeRepository.existsByEmail("asha.rao@example.com"));
        assertFalse(employeeRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void testFindByDepartment() {
        Page<Employee> engEmployees = employeeRepository.findByDepartment(Department.ENGINEERING, PageRequest.of(0, 10));
        assertEquals(1, engEmployees.getTotalElements());
        assertEquals("Asha", engEmployees.getContent().get(0).getFirstName());
    }

    @Test
    void testSearchEmployeesWithKeyword() {
        Page<Employee> results = employeeRepository.searchEmployees(null, "Vikram", PageRequest.of(0, 10));
        assertEquals(1, results.getTotalElements());
        assertEquals("Vikram", results.getContent().get(0).getFirstName());
    }
}
