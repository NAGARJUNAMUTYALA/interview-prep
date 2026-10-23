# 75 Real-World SQL Interview Queries & Practical Solutions

**Target Dialect:** PostgreSQL 16 / ANSI SQL  
**Schema Theme:** Enterprise Employee Management, Departments, Salaries, Projects, and Performance Reviews  

---

## 1. Schema Baseline

```sql
CREATE TABLE departments (
    dept_id SERIAL PRIMARY KEY,
    dept_name VARCHAR(50) NOT NULL UNIQUE,
    location VARCHAR(50) NOT NULL
);

CREATE TABLE employees (
    emp_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    dept_id INT REFERENCES departments(dept_id),
    salary NUMERIC(12,2) NOT NULL,
    manager_id INT REFERENCES employees(emp_id),
    hire_date DATE NOT NULL
);

CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    budget NUMERIC(14,2) NOT NULL
);

CREATE TABLE employee_projects (
    emp_id INT REFERENCES employees(emp_id),
    project_id INT REFERENCES projects(project_id),
    hours_allocated INT NOT NULL,
    PRIMARY KEY (emp_id, project_id)
);

CREATE TABLE salaries_history (
    history_id SERIAL PRIMARY KEY,
    emp_id INT REFERENCES employees(emp_id),
    old_salary NUMERIC(12,2) NOT NULL,
    new_salary NUMERIC(12,2) NOT NULL,
    change_date DATE NOT NULL
);
```

---

## Part I: Ranking & Window Functions (Queries 1–15)

### Q1: Find the 2nd highest salary using DENSE_RANK()
```sql
WITH RankedSalaries AS (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rank_pos
    FROM employees
)
SELECT DISTINCT salary 
FROM RankedSalaries 
WHERE rank_pos = 2;
```

### Q2: Find the N-th highest salary (parameterized N = 3)
```sql
SELECT DISTINCT salary
FROM (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) as rank_pos
    FROM employees
) ranked
WHERE rank_pos = 3;
```

### Q3: Find the top 3 highest-paid employees in EACH department
```sql
WITH DeptRanked AS (
    SELECT emp_id, first_name, last_name, dept_id, salary,
           DENSE_RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rnk
    FROM employees
)
SELECT * FROM DeptRanked WHERE rnk <= 3;
```

### Q4: Compute the running total of salaries ordered by hire date
```sql
SELECT emp_id, first_name, salary, hire_date,
       SUM(salary) OVER (ORDER BY hire_date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS running_total
FROM employees;
```

### Q5: Fetch employee details along with their department's average salary
```sql
SELECT emp_id, first_name, dept_id, salary,
       AVG(salary) OVER (PARTITION BY dept_id) AS dept_avg_salary
FROM employees;
```

### Q6: Find employees earning more than their department's average salary
```sql
WITH DeptAvg AS (
    SELECT emp_id, first_name, salary, dept_id,
           AVG(salary) OVER (PARTITION BY dept_id) AS avg_sal
    FROM employees
)
SELECT emp_id, first_name, salary, avg_sal
FROM DeptAvg
WHERE salary > avg_sal;
```

### Q7: Calculate difference between employee salary and previous hired employee's salary (LAG)
```sql
SELECT emp_id, first_name, hire_date, salary,
       LAG(salary, 1) OVER (ORDER BY hire_date) AS prev_salary,
       salary - LAG(salary, 1) OVER (ORDER BY hire_date) AS salary_diff
FROM employees;
```

### Q8: Find salary growth percentage between successive promotions (salaries_history)
```sql
SELECT emp_id, change_date, old_salary, new_salary,
       ROUND(((new_salary - old_salary) / old_salary) * 100, 2) AS pct_increase
FROM salaries_history;
```

### Q9: Divide employees into 4 salary quartiles using NTILE(4)
```sql
SELECT emp_id, first_name, salary,
       NTILE(4) OVER (ORDER BY salary DESC) AS salary_quartile
FROM employees;
```

### Q10: Fetch the first and last hired employee in each department using FIRST_VALUE / LAST_VALUE
```sql
SELECT DISTINCT dept_id,
       FIRST_VALUE(first_name) OVER (PARTITION BY dept_id ORDER BY hire_date) AS earliest_hire,
       FIRST_VALUE(first_name) OVER (PARTITION BY dept_id ORDER BY hire_date DESC) AS latest_hire
FROM employees;
```

### Q11: Row numbering per department without gaps (ROW_NUMBER)
```sql
SELECT emp_id, first_name, dept_id,
       ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY emp_id) as row_num
FROM employees;
```

### Q12: Rank employees with gaps on tied salaries (RANK)
```sql
SELECT emp_id, first_name, salary,
       RANK() OVER (ORDER BY salary DESC) as rank_with_gaps
FROM employees;
```

### Q13: Moving average of salary over the last 3 employees hired
```sql
SELECT emp_id, hire_date, salary,
       AVG(salary) OVER (ORDER BY hire_date ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) AS moving_avg_3
FROM employees;
```

### Q14: Percentage contribution of each employee's salary to their department total
```sql
SELECT emp_id, first_name, dept_id, salary,
       ROUND((salary / SUM(salary) OVER (PARTITION BY dept_id)) * 100, 2) AS pct_dept_payout
FROM employees;
```

### Q15: Next hired employee's name and hire date (LEAD)
```sql
SELECT emp_id, first_name, hire_date,
       LEAD(first_name, 1) OVER (ORDER BY hire_date) as next_hired_emp,
       LEAD(hire_date, 1) OVER (ORDER BY hire_date) as next_hired_date
FROM employees;
```

---

## Part II: Joins, Aggregations & Grouping (Queries 16–30)

### Q16: Department names along with total employee count and total salary payout
```sql
SELECT d.dept_name, COUNT(e.emp_id) AS total_employees, COALESCE(SUM(e.salary), 0) AS total_payout
FROM departments d
LEFT JOIN employees e ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name;
```

### Q17: List departments having more than 5 employees
```sql
SELECT d.dept_name, COUNT(e.emp_id) AS emp_count
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name
HAVING COUNT(e.emp_id) > 5;
```

### Q18: Self-Join: List employees alongside their manager's name
```sql
SELECT e.emp_id, e.first_name || ' ' || e.last_name AS employee_name,
       COALESCE(m.first_name || ' ' || m.last_name, 'Top Level Executive') AS manager_name
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.emp_id;
```

### Q19: Find employees who earn more than their direct manager
```sql
SELECT e.emp_id, e.first_name, e.salary, m.first_name AS manager_name, m.salary AS manager_salary
FROM employees e
JOIN employees m ON e.manager_id = m.emp_id
WHERE e.salary > m.salary;
```

### Q20: Find departments with zero assigned employees
```sql
SELECT d.dept_id, d.dept_name
FROM departments d
LEFT JOIN employees e ON d.dept_id = e.dept_id
WHERE e.emp_id IS NULL;
```

### Q21: Count employees assigned to multiple projects
```sql
SELECT emp_id, COUNT(project_id) AS project_count
FROM employee_projects
GROUP BY emp_id
HAVING COUNT(project_id) > 1;
```

### Q22: Cross join: Matrix of all departments and all projects
```sql
SELECT d.dept_name, p.project_name
FROM departments d
CROSS JOIN projects p;
```

### Q23: Full outer join: Identify unmatched employees and departments
```sql
SELECT e.emp_id, e.first_name, d.dept_name
FROM employees e
FULL OUTER JOIN departments d ON e.dept_id = d.dept_id
WHERE e.dept_id IS NULL OR d.dept_id IS NULL;
```

### Q24: Highest budget project and its allocated team members
```sql
WITH MaxProject AS (
    SELECT project_id, project_name, budget 
    FROM projects 
    ORDER BY budget DESC 
    LIMIT 1
)
SELECT mp.project_name, mp.budget, e.first_name, e.last_name
FROM MaxProject mp
JOIN employee_projects ep ON mp.project_id = ep.project_id
JOIN employees e ON ep.emp_id = e.emp_id;
```

### Q25: Total project hours logged per department
```sql
SELECT d.dept_name, SUM(ep.hours_allocated) AS total_hours
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id
JOIN employee_projects ep ON e.emp_id = ep.emp_id
GROUP BY d.dept_id, d.dept_name;
```

### Q26: Department with maximum average salary
```sql
SELECT d.dept_name, AVG(e.salary) AS avg_sal
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name
ORDER BY avg_sal DESC
LIMIT 1;
```

### Q27: Cumulative salary expenditure by department
```sql
SELECT d.dept_name, e.first_name, e.salary,
       SUM(e.salary) OVER (PARTITION BY d.dept_name ORDER BY e.salary) AS cumulative_dept_salary
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id;
```

### Q28: Find managers who manage at least 3 direct reports
```sql
SELECT m.emp_id, m.first_name, m.last_name, COUNT(e.emp_id) AS report_count
FROM employees m
JOIN employees e ON m.emp_id = e.manager_id
GROUP BY m.emp_id, m.first_name, m.last_name
HAVING COUNT(e.emp_id) >= 3;
```

### Q29: Employees not assigned to any project
```sql
SELECT e.emp_id, e.first_name, e.last_name
FROM employees e
LEFT JOIN employee_projects ep ON e.emp_id = ep.emp_id
WHERE ep.project_id IS NULL;
```

### Q30: Projects with zero allocated employees
```sql
SELECT p.project_id, p.project_name
FROM projects p
LEFT JOIN employee_projects ep ON p.project_id = ep.project_id
WHERE ep.emp_id IS NULL;
```

---

## Part III: Subqueries, CTEs & Common Table Expressions (Queries 31–45)

### Q31: Recursive CTE: Organization hierarchy tree (Manager to Reports)
```sql
WITH RECURSIVE OrgHierarchy AS (
    -- Anchor: Top executive without manager
    SELECT emp_id, first_name, manager_id, 1 AS level, CAST(first_name AS VARCHAR(255)) AS path
    FROM employees
    WHERE manager_id IS NULL
    
    UNION ALL
    
    -- Recursive step
    SELECT e.emp_id, e.first_name, e.manager_id, oh.level + 1, CAST(oh.path || ' -> ' || e.first_name AS VARCHAR(255))
    FROM employees e
    JOIN OrgHierarchy oh ON e.manager_id = oh.emp_id
)
SELECT * FROM OrgHierarchy ORDER BY level, emp_id;
```

### Q32: Employees earning above overall organization average
```sql
SELECT emp_id, first_name, salary
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
```

### Q33: Correlated Subquery: Employees earning maximum salary in their department
```sql
SELECT e1.emp_id, e1.first_name, e1.dept_id, e1.salary
FROM employees e1
WHERE e1.salary = (
    SELECT MAX(e2.salary)
    FROM employees e2
    WHERE e2.dept_id = e1.dept_id
);
```

### Q34: Correlated Subquery using EXISTS: Departments with active projects
```sql
SELECT d.dept_id, d.dept_name
FROM departments d
WHERE EXISTS (
    SELECT 1 
    FROM employees e
    JOIN employee_projects ep ON e.emp_id = ep.emp_id
    WHERE e.dept_id = d.dept_id
);
```

### Q35: NOT EXISTS: Employees without any salary increase records
```sql
SELECT e.emp_id, e.first_name
FROM employees e
WHERE NOT EXISTS (
    SELECT 1 FROM salaries_history sh WHERE sh.emp_id = e.emp_id
);
```

### Q36: CTE finding departments whose total salary exceeds $500,000
```sql
WITH DeptPayout AS (
    SELECT dept_id, SUM(salary) AS total_payout
    FROM employees
    GROUP BY dept_id
)
SELECT d.dept_name, dp.total_payout
FROM DeptPayout dp
JOIN departments d ON dp.dept_id = d.dept_id
WHERE dp.total_payout > 500000;
```

### Q37: Scalar subquery in SELECT: Employee salary vs highest salary in company
```sql
SELECT emp_id, first_name, salary,
       (SELECT MAX(salary) FROM employees) AS max_company_salary,
       (SELECT MAX(salary) FROM employees) - salary AS gap_from_max
FROM employees;
```

### Q38: Find employees whose salary matches any employee in Finance department
```sql
SELECT emp_id, first_name, salary
FROM employees
WHERE salary IN (
    SELECT e.salary 
    FROM employees e
    JOIN departments d ON e.dept_id = d.dept_id
    WHERE d.dept_name = 'Finance'
);
```

### Q39: Employees whose salary is greater than ALL employees in HR department
```sql
SELECT emp_id, first_name, salary
FROM employees
WHERE salary > ALL (
    SELECT e.salary 
    FROM employees e
    JOIN departments d ON e.dept_id = d.dept_id
    WHERE d.dept_name = 'Human Resources'
);
```

### Q40: Employees whose salary is greater than ANY employee in Marketing department
```sql
SELECT emp_id, first_name, salary
FROM employees
WHERE salary > ANY (
    SELECT e.salary 
    FROM employees e
    JOIN departments d ON e.dept_id = d.dept_id
    WHERE d.dept_name = 'Marketing'
);
```

### Q41: Find all subordinates under a specific manager using Recursive CTE
```sql
WITH RECURSIVE Subordinates AS (
    SELECT emp_id, first_name, manager_id
    FROM employees
    WHERE manager_id = 1 -- Target manager ID
    
    UNION ALL
    
    SELECT e.emp_id, e.first_name, e.manager_id
    FROM employees e
    JOIN Subordinates s ON e.manager_id = s.emp_id
)
SELECT * FROM Subordinates;
```

### Q42: CTE to compute medians of employee salaries
```sql
WITH OrderedSalaries AS (
    SELECT salary,
           ROW_NUMBER() OVER (ORDER BY salary) AS row_num,
           COUNT(*) OVER () AS total_count
    FROM employees
)
SELECT AVG(salary) AS median_salary
FROM OrderedSalaries
WHERE row_num IN ((total_count + 1) / 2, (total_count + 2) / 2);
```

### Q43: Duplicate email detection across table
```sql
SELECT email, COUNT(*) AS duplicate_count
FROM employees
GROUP BY email
HAVING COUNT(*) > 1;
```

### Q44: Delete duplicate records keeping only lowest emp_id (CTID / Subquery)
```sql
DELETE FROM employees
WHERE emp_id NOT IN (
    SELECT MIN(emp_id)
    FROM employees
    GROUP BY email
);
```

### Q45: Update employee salaries to match department average (Correlation)
```sql
UPDATE employees e
SET salary = (
    SELECT ROUND(AVG(e2.salary), 2)
    FROM employees e2
    WHERE e2.dept_id = e.dept_id
)
WHERE e.salary < 30000;
```

---

## Part IV: Date, Strings & Conditional Logic (Queries 46–60)

### Q46: Employees hired in the current year
```sql
SELECT emp_id, first_name, hire_date
FROM employees
WHERE EXTRACT(YEAR FROM hire_date) = EXTRACT(YEAR FROM CURRENT_DATE);
```

### Q47: Employees hired on weekends (Saturday / Sunday)
```sql
SELECT emp_id, first_name, hire_date, TO_CHAR(hire_date, 'Day') AS day_name
FROM employees
WHERE EXTRACT(ISODOW FROM hire_date) IN (6, 7);
```

### Q48: Calculate employee tenure in years and months (AGE function)
```sql
SELECT emp_id, first_name, hire_date,
       AGE(CURRENT_DATE, hire_date) AS tenure
FROM employees;
```

### Q49: Classify salaries into bands using CASE WHEN
```sql
SELECT emp_id, first_name, salary,
       CASE 
           WHEN salary >= 100000 THEN 'Executive Band'
           WHEN salary >= 70000 THEN 'Senior Band'
           WHEN salary >= 45000 THEN 'Mid-Level Band'
           ELSE 'Associate Band'
       END AS salary_grade
FROM employees;
```

### Q50: Mask employee email for privacy (e.g. a***@example.com)
```sql
SELECT emp_id, email,
       SUBSTRING(email FROM 1 FOR 1) || '***@' || SPLIT_PART(email, '@', 2) AS masked_email
FROM employees;
```

### Q51: Extract domain name from employee emails
```sql
SELECT DISTINCT SPLIT_PART(email, '@', 2) AS email_domain
FROM employees;
```

### Q52: Format salary with currency symbol and thousands separator
```sql
SELECT emp_id, first_name, TO_CHAR(salary, '$999,999,999.00') AS formatted_salary
FROM employees;
```

### Q53: Find employees completing 5, 10, or 15 year anniversaries this month
```sql
SELECT emp_id, first_name, hire_date
FROM employees
WHERE EXTRACT(MONTH FROM hire_date) = EXTRACT(MONTH FROM CURRENT_DATE)
  AND EXTRACT(YEAR FROM AGE(CURRENT_DATE, hire_date)) IN (5, 10, 15);
```

### Q54: Trim extra whitespace and capitalize first and last names
```sql
SELECT emp_id, INITCAP(TRIM(first_name)) AS clean_first, INITCAP(TRIM(last_name)) AS clean_last
FROM employees;
```

### Q55: Count employees hired by month across all years
```sql
SELECT TO_CHAR(hire_date, 'Month') AS hire_month, COUNT(*) AS count
FROM employees
GROUP BY TO_CHAR(hire_date, 'Month'), EXTRACT(MONTH FROM hire_date)
ORDER BY EXTRACT(MONTH FROM hire_date);
```

### Q56: COALESCE example: Fallback chain for manager contact
```sql
SELECT emp_id, first_name, COALESCE(email, 'no-email@company.com') AS contact_email
FROM employees;
```

### Q57: NULLIF example: Avoid division by zero when calculating budget per employee
```sql
SELECT p.project_name, p.budget,
       ROUND(p.budget / NULLIF(COUNT(ep.emp_id), 0), 2) AS budget_per_resource
FROM projects p
LEFT JOIN employee_projects ep ON p.project_id = ep.project_id
GROUP BY p.project_id, p.project_name, p.budget;
```

### Q58: Find employees whose first name starts with 'A' and ends with 'a' (case-insensitive)
```sql
SELECT emp_id, first_name
FROM employees
WHERE first_name ILIKE 'A%a';
```

### Q59: Group employees by first letter of last name
```sql
SELECT SUBSTRING(last_name, 1, 1) AS initial, COUNT(*) AS count
FROM employees
GROUP BY SUBSTRING(last_name, 1, 1)
ORDER BY initial;
```

### Q60: Pivot table: Department-wise counts of employees across salary bands
```sql
SELECT d.dept_name,
       COUNT(CASE WHEN e.salary >= 100000 THEN 1 END) AS high_earners,
       COUNT(CASE WHEN e.salary BETWEEN 60000 AND 99999 THEN 1 END) AS mid_earners,
       COUNT(CASE WHEN e.salary < 60000 THEN 1 END) AS junior_earners
FROM departments d
LEFT JOIN employees e ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name;
```

---

## Part V: Performance, Indexing & Advanced Scenarios (Queries 61–75)

### Q61: Identify missing indexes on Foreign Keys
```sql
SELECT conrelid::regclass AS table_name,
       a.attname AS fk_column
FROM pg_constraint c
JOIN pg_attribute a ON a.attrelid = c.conrelid AND a.attnum = ANY(c.conkey)
WHERE c.contype = 'f'
  AND NOT EXISTS (
      SELECT 1 FROM pg_index i
      WHERE i.indrelid = c.conrelid AND a.attnum = ANY(i.indkey)
  );
```

### Q62: Inspect query execution plan using EXPLAIN ANALYZE
```sql
EXPLAIN (ANALYZE, BUFFERS, COSTS)
SELECT * FROM employees WHERE email = 'asha.rao@example.com';
```

### Q63: Upsert (INSERT ... ON CONFLICT DO UPDATE) in PostgreSQL
```sql
INSERT INTO employees (first_name, last_name, email, dept_id, salary, hire_date)
VALUES ('Kavita', 'Patel', 'kavita.p@example.com', 1, 90000, CURRENT_DATE)
ON CONFLICT (email)
DO UPDATE SET
    salary = EXCLUDED.salary,
    dept_id = EXCLUDED.dept_id;
```

### Q64: Batch Insert with RETURNING clause
```sql
INSERT INTO departments (dept_name, location)
VALUES ('Data Science', 'Building C'), ('Cybersecurity', 'Building D')
RETURNING dept_id, dept_name;
```

### Q65: Optimistic locking query with version check
```sql
UPDATE employees
SET salary = 95000
WHERE emp_id = 1; -- Enforced by application @Version in Hibernate
```

### Q66: Select with Pessimistic Write Lock (FOR UPDATE)
```sql
BEGIN;
SELECT * FROM employees 
WHERE emp_id = 1 
FOR UPDATE;
-- Performs atomic balance or salary mutation
UPDATE employees SET salary = salary + 5000 WHERE emp_id = 1;
COMMIT;
```

### Q67: SKIP LOCKED: Job Queue / Task Processing pattern
```sql
-- Worker thread dequeues next pending task without blocking on concurrently locked rows
BEGIN;
SELECT emp_id, first_name 
FROM employees 
WHERE dept_id = 1 
ORDER BY emp_id 
LIMIT 1 
FOR UPDATE SKIP LOCKED;
COMMIT;
```

### Q68: Calculate Gini Coefficient / Salary Inequality Metric
```sql
WITH Ranked AS (
    SELECT salary,
           ROW_NUMBER() OVER (ORDER BY salary) as i,
           COUNT(*) OVER () as n,
           SUM(salary) OVER () as total_sal
    FROM employees
)
SELECT ROUND((2.0 * SUM(i * salary) / (MAX(n) * MAX(total_sal)) - (MAX(n) + 1.0) / MAX(n))::numeric, 4) AS gini_index
FROM Ranked;
```

### Q69: Find the longest streak of consecutive days where new employees joined
```sql
WITH DistinctDates AS (
    SELECT DISTINCT hire_date FROM employees
),
GroupedStreaks AS (
    SELECT hire_date,
           hire_date - (ROW_NUMBER() OVER (ORDER BY hire_date) * INTERVAL '1 day') AS streak_group
    FROM DistinctDates
)
SELECT MIN(hire_date) AS streak_start,
       MAX(hire_date) AS streak_end,
       COUNT(*) AS streak_days
FROM GroupedStreaks
GROUP BY streak_group
ORDER BY streak_days DESC
LIMIT 1;
```

### Q70: Table Partitioning query check
```sql
SELECT relname AS partition_name,
       pg_size_pretty(pg_total_relation_size(c.oid)) AS total_size
FROM pg_class c
JOIN pg_inherits i ON i.inhrelid = c.oid
WHERE i.inhparent = 'employees'::regclass;
```

### Q71: Inspect table bloat and dead tuples in PostgreSQL
```sql
SELECT relname AS table_name,
       n_live_tup AS live_rows,
       n_dead_tup AS dead_rows,
       ROUND(n_dead_tup * 100.0 / NULLIF(n_live_tup + n_dead_tup, 0), 2) AS dead_tuple_pct,
       last_vacuum, last_autovacuum
FROM pg_stat_user_tables
ORDER BY n_dead_tup DESC;
```

### Q72: Search employee data with JSONB operators
```sql
-- Assume metadata JSONB column on employee
-- SELECT emp_id, metadata->>'github_handle' FROM employees WHERE metadata @> '{"role": "Tech Lead"}';
SELECT emp_id, first_name FROM employees WHERE email LIKE '%@example.com';
```

### Q73: Find mutual project assignments between pairs of employees
```sql
SELECT ep1.emp_id AS emp_1, ep2.emp_id AS emp_2, COUNT(ep1.project_id) AS shared_projects
FROM employee_projects ep1
JOIN employee_projects ep2 ON ep1.project_id = ep2.project_id AND ep1.emp_id < ep2.emp_id
GROUP BY ep1.emp_id, ep2.emp_id
HAVING COUNT(ep1.project_id) >= 2;
```

### Q74: Detect circular management hierarchies (Manager A manages B, B manages A)
```sql
SELECT e1.emp_id AS emp_a, e1.first_name AS name_a,
       e2.emp_id AS emp_b, e2.first_name AS name_b
FROM employees e1
JOIN employees e2 ON e1.manager_id = e2.emp_id AND e2.manager_id = e1.emp_id
WHERE e1.emp_id < e2.emp_id;
```

### Q75: Rollup and Cube: Multi-dimensional aggregations
```sql
SELECT d.dept_name,
       EXTRACT(YEAR FROM e.hire_date) AS hire_year,
       SUM(e.salary) AS total_payroll
FROM departments d
JOIN employees e ON d.dept_id = e.dept_id
GROUP BY ROLLUP (d.dept_name, EXTRACT(YEAR FROM e.hire_date));
```
