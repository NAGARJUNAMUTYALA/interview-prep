package com.example.interviewprep.employee.repository;

import com.example.interviewprep.employee.entity.Department;
import com.example.interviewprep.employee.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Reusable and composable JPA Specifications for dynamic query building using the Criteria API.
 */
public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> hasDepartment(Department department) {
        return (root, query, cb) -> department == null ? null : cb.equal(root.get("department"), department);
    }

    public static Specification<Employee> salaryGreaterThanOrEqualTo(BigDecimal minSalary) {
        return (root, query, cb) -> minSalary == null ? null : cb.greaterThanOrEqualTo(root.get("salary"), minSalary);
    }

    public static Specification<Employee> salaryLessThanOrEqualTo(BigDecimal maxSalary) {
        return (root, query, cb) -> maxSalary == null ? null : cb.lessThanOrEqualTo(root.get("salary"), maxSalary);
    }

    public static Specification<Employee> searchKeyword(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }

    public static Specification<Employee> buildSpecification(
            Department department,
            String search,
            BigDecimal minSalary,
            BigDecimal maxSalary
    ) {
        return Specification.where(hasDepartment(department))
                .and(searchKeyword(search))
                .and(salaryGreaterThanOrEqualTo(minSalary))
                .and(salaryLessThanOrEqualTo(maxSalary));
    }
}
