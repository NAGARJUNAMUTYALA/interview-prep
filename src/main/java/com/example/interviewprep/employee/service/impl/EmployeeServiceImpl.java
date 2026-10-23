package com.example.interviewprep.employee.service.impl;

import com.example.interviewprep.common.exception.DuplicateResourceException;
import com.example.interviewprep.common.exception.ResourceNotFoundException;
import com.example.interviewprep.employee.dto.EmployeeRequest;
import com.example.interviewprep.employee.dto.EmployeeResponse;
import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.entity.Employee;
import com.example.interviewprep.employee.repository.EmployeeRepository;
import com.example.interviewprep.employee.repository.EmployeeSpecification;
import com.example.interviewprep.employee.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Production implementation of {@link EmployeeService}.
 * Encapsulates domain logic, validation checks, exception propagation,
 * and declarative transaction management.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Creates and persists a new Employee in the database.
     *
     * <ul>
     *   <li><b>Purpose:</b> Onboard a new employee after enforcing business uniqueness rules.</li>
     *   <li><b>Input:</b> Validated {@link EmployeeRequest} containing name, email, department, and salary.</li>
     *   <li><b>Output:</b> Immutable {@link EmployeeResponse} reflecting the persisted state with generated ID and timestamps.</li>
     *   <li><b>Validation & Rules:</b> Email must be globally unique across all active employees.</li>
     *   <li><b>Repository Interaction:</b> Calls {@link EmployeeRepository#existsByEmail(String)} prior to saving, then {@link EmployeeRepository#save(Object)}.</li>
     *   <li><b>Transaction Behavior:</b> {@code @Transactional} (read-write). If email uniqueness or database constraints fail, the transaction rolls back.</li>
     *   <li><b>Exception Scenarios:</b> Throws {@link DuplicateResourceException} (HTTP 409) if the email is already registered.</li>
     *   <li><b>Design Decision:</b> Explicit pre-check with {@code existsByEmail} provides clean domain errors before hitting low-level DB unique constraint violations.</li>
     * </ul>
     */
    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        log.info("Attempting to create employee with email: {}", request.email());

        if (employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("An employee with email '" + request.email() + "' already exists");
        }

        Employee employee = new Employee(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.department(),
                request.salary()
        );

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully created employee with id: {}", savedEmployee.getId());
        return EmployeeResponse.fromEntity(savedEmployee);
    }

    /**
     * Retrieves an Employee by their unique database identifier.
     *
     * <ul>
     *   <li><b>Purpose:</b> Fetch single employee details for display, editing, or auditing.</li>
     *   <li><b>Input:</b> {@code Long id} - Non-null database identifier.</li>
     *   <li><b>Output:</b> {@link EmployeeResponse} mapping the entity fields.</li>
     *   <li><b>Validation & Rules:</b> ID must exist in the persistence store.</li>
     *   <li><b>Repository Interaction:</b> Executes {@link EmployeeRepository#findById(Object)}.</li>
     *   <li><b>Transaction Behavior:</b> Read-only transaction optimization (avoids Hibernate dirty-checking overhead).</li>
     *   <li><b>Exception Scenarios:</b> Throws {@link ResourceNotFoundException} (HTTP 404) if no entity matches the provided ID.</li>
     *   <li><b>Design Decision:</b> Maps entity to an immutable Java Record DTO immediately to prevent exposing internal JPA entities.</li>
     * </ul>
     */
    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        log.debug("Fetching employee by id: {}", id);
        return employeeRepository.findById(id)
                .map(EmployeeResponse::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    /**
     * Retrieves a paginated and filtered page of employees using JPQL.
     */
    @Override
    public Page<EmployeeResponse> getAllEmployees(Department department, String search, Pageable pageable) {
        return getAllEmployees(department, search, null, null, pageable);
    }

    /**
     * Retrieves a paginated and dynamically filtered page of employees using composable JPA Specifications.
     *
     * <ul>
     *   <li><b>Purpose:</b> Power enterprise UI search filters with salary range, department, and full-text keyword criteria.</li>
     *   <li><b>Input:</b> Optional {@link Department}, search string, {@code minSalary}, {@code maxSalary}, and {@link Pageable}.</li>
     *   <li><b>Output:</b> {@link Page} of {@link EmployeeResponse} objects.</li>
     *   <li><b>Repository Interaction:</b> Executes {@link EmployeeRepository#findAll(Specification, Pageable)}.</li>
     *   <li><b>Transaction Behavior:</b> Read-only transaction.</li>
     *   <li><b>Design Decision:</b> JPA Criteria API dynamically builds SQL WHERE clauses avoiding SQL injection and string concatenation bugs.</li>
     * </ul>
     */
    @Override
    public Page<EmployeeResponse> getAllEmployees(
            Department department,
            String search,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            Pageable pageable
    ) {
        log.debug("Fetching employees with criteria - dept: {}, search: {}, minSalary: {}, maxSalary: {}",
                department, search, minSalary, maxSalary);

        Specification<Employee> spec = EmployeeSpecification.buildSpecification(department, search, minSalary, maxSalary);
        return employeeRepository.findAll(spec, pageable).map(EmployeeResponse::fromEntity);
    }

    /**
     * Updates an existing Employee entity with new details.
     */
    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        log.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));

        if (!employee.getEmail().equalsIgnoreCase(request.email())) {
            if (employeeRepository.existsByEmail(request.email())) {
                throw new DuplicateResourceException("An employee with email '" + request.email() + "' already exists");
            }
            employee.setEmail(request.email());
        }

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setDepartment(request.department());
        employee.setSalary(request.salary());

        Employee updated = employeeRepository.save(employee);
        log.info("Successfully updated employee with id: {}", updated.getId());
        return EmployeeResponse.fromEntity(updated);
    }

    /**
     * Deletes an Employee by ID.
     */
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        }
        employeeRepository.deleteById(id);
        log.info("Successfully deleted employee with id: {}", id);
    }
}
