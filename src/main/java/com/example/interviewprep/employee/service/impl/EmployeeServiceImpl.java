package com.example.interviewprep.employee.service.impl;

import com.example.interviewprep.common.exception.DuplicateResourceException;
import com.example.interviewprep.common.exception.ResourceNotFoundException;
import com.example.interviewprep.employee.dto.EmployeeRequest;
import com.example.interviewprep.employee.dto.EmployeeResponse;
import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.entity.Employee;
import com.example.interviewprep.employee.repository.EmployeeRepository;
import com.example.interviewprep.employee.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Retrieves a paginated and filtered page of employees.
     *
     * <ul>
     *   <li><b>Purpose:</b> Power enterprise UI tables with server-side pagination, sorting, and multi-field search.</li>
     *   <li><b>Input:</b> Optional {@link Department}, optional search keyword (matched against first name, last name, or email), and {@link Pageable}.</li>
     *   <li><b>Output:</b> {@link Page} of {@link EmployeeResponse} objects including total counts and pagination metadata.</li>
     *   <li><b>Repository Interaction:</b> Executes JPQL query {@link EmployeeRepository#searchEmployees(Department, String, Pageable)}.</li>
     *   <li><b>Transaction Behavior:</b> Read-only transaction.</li>
     *   <li><b>Design Decision:</b> Uses Spring Data JPA {@code Page.map()} to transform entities into DTOs within the active persistence context.</li>
     * </ul>
     */
    @Override
    public Page<EmployeeResponse> getAllEmployees(Department department, String search, Pageable pageable) {
        log.debug("Fetching employees with department: {}, search: {}, page: {}", department, search, pageable.getPageNumber());
        String effectiveSearch = (search != null && !search.isBlank()) ? search.trim() : null;
        return employeeRepository.searchEmployees(department, effectiveSearch, pageable)
                .map(EmployeeResponse::fromEntity);
    }

    /**
     * Updates an existing Employee entity with new details.
     *
     * <ul>
     *   <li><b>Purpose:</b> Modify employee attributes while safeguarding identity and uniqueness constraints.</li>
     *   <li><b>Input:</b> {@code Long id} and updated {@link EmployeeRequest}.</li>
     *   <li><b>Output:</b> Updated {@link EmployeeResponse}.</li>
     *   <li><b>Validation & Rules:</b> Employee must exist. If email is changed, the new email must not belong to another employee.</li>
     *   <li><b>Repository Interaction:</b> Loads existing entity via {@code findById}, checks email uniqueness if altered, updates fields via setters.</li>
     *   <li><b>Transaction Behavior:</b> {@code @Transactional} (read-write). Relies on Hibernate dirty-checking to flush updates automatically upon commit.</li>
     *   <li><b>Exception Scenarios:</b> Throws {@link ResourceNotFoundException} if ID is missing; throws {@link DuplicateResourceException} if target email is taken.</li>
     *   <li><b>Design Decision:</b> Modifies managed entity in-place rather than creating a new instance, enabling Hibernate automatic dirty checking.</li>
     * </ul>
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

        // Dirty checking automatically flushes changes at transaction commit; save is called for clarity
        Employee updated = employeeRepository.save(employee);
        log.info("Successfully updated employee with id: {}", updated.getId());
        return EmployeeResponse.fromEntity(updated);
    }

    /**
     * Deletes an Employee by ID.
     *
     * <ul>
     *   <li><b>Purpose:</b> Remove an employee record from the system.</li>
     *   <li><b>Input:</b> {@code Long id} of the employee to remove.</li>
     *   <li><b>Validation & Rules:</b> Employee must exist prior to deletion.</li>
     *   <li><b>Repository Interaction:</b> {@link EmployeeRepository#existsById(Object)} followed by {@link EmployeeRepository#deleteById(Object)}.</li>
     *   <li><b>Transaction Behavior:</b> {@code @Transactional} (read-write).</li>
     *   <li><b>Exception Scenarios:</b> Throws {@link ResourceNotFoundException} if the ID is not found.</li>
     *   <li><b>Design Decision:</b> Explicit existence check provides strict idempotent deletion semantics (404 on missing record).</li>
     * </ul>
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
