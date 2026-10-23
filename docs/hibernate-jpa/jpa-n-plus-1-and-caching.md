# Hibernate & JPA Internals: Persistence Context, N+1 Problem & Locking

**Target Audience:** Senior Java Backend Engineers  
**Topic Frequency:** Top 3 most frequent topic in Java backend rounds  

---

## 1. Entity Lifecycle States

```text
       new Employee()
             │
             ▼
        [TRANSIENT]
       (Not in DB, not in Persistence Context)
             │
             │ entityManager.persist()
             ▼
        [MANAGED / PERSISTENT] ◄────────┐
       (In DB, in 1st-level cache)      │
             │                          │ entityManager.merge()
             │                          │
             ├──────────────────────────┤
             │                          │
             │ entityManager.detach()   │
             │ or session.close()       │
             ▼                          │
        [DETACHED] ─────────────────────┘
       (In DB, NOT in Persistence Context)
             │
             │ entityManager.remove()
             ▼
        [REMOVED]
       (Deleted from DB upon commit)
```

### The First-Level Cache & Dirty Checking
1. The **Persistence Context** acts as an in-memory cache for a single transaction/EntityManager.
2. When an entity is loaded, Hibernate takes an initial snapshot of its state.
3. When setters are called (`employee.setSalary(95000)`), Hibernate does **not** issue an `UPDATE` query immediately.
4. Upon transaction commit (`@Transactional`), Hibernate flushes: it compares the entity's current state with the initial snapshot. If differences exist, it automatically issues an optimized SQL `UPDATE` statement.
5. Setting `@Transactional(readOnly = true)` informs Hibernate not to create initial snapshots, cutting memory consumption and CPU comparison cycles in half.

---

## 2. The N+1 Query Problem & Remediation

### The Symptom:
Fetching $N$ employees with a `@ManyToOne` department relationship executes **1 query** for employees, followed by **N individual queries** for each employee's department!

```sql
-- 1 query to fetch employees
SELECT * FROM employees;

-- N queries to fetch department for each employee
SELECT * FROM departments WHERE dept_id = 1;
SELECT * FROM departments WHERE dept_id = 2;
... (N times)
```

### The Solution:

#### Option 1: JPQL `JOIN FETCH`
```java
@Query("SELECT e FROM Employee e JOIN FETCH e.department WHERE e.salary > :minSalary")
List<Employee> findHighEarnersWithDepartment(@Param("minSalary") BigDecimal minSalary);
```
*Generated SQL:*
```sql
SELECT e.*, d.* FROM employees e 
INNER JOIN departments d ON e.dept_id = d.dept_id 
WHERE e.salary > ?;
```
Executes in **1 single SQL query**!

#### Option 2: `@EntityGraph`
```java
@EntityGraph(attributePaths = {"department"})
List<Employee> findBySalaryGreaterThan(BigDecimal minSalary);
```
Applies a SQL LEFT OUTER JOIN dynamically without rewriting JPQL.

---

## 3. Optimistic vs. Pessimistic Locking

| Strategy | When to Use | Mechanism | Database Locks |
|---|---|---|---|
| **Optimistic Locking** (`@Version`) | Low write contention, high read volume (web apps) | A numeric version column (`version INT`) is incremented on each update: `WHERE id = ? AND version = ?`. If version changed, `OptimisticLockException` is thrown. | **No database locks held.** Maximum concurrency. |
| **Pessimistic Locking** (`PESSIMISTIC_WRITE`) | High write contention (banking, inventory reservation, ticket booking) | Issues a database row lock (`SELECT ... FOR UPDATE`). Other concurrent transactions wait until the lock is released upon transaction commit. | **Exclusive row lock held** in DB for transaction duration. |
