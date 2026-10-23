# Core Java & JVM Memory: Complete Senior Interview Deep Dive

**Target Audience:** Mid-to-Senior Java Backend Engineers (~5 Years Experience)  
**Topics Covered:** OOP, JMM, Pass-by-Value, String Constant Pool, Object Contracts, Generics, Immutability, and Exception Hierarchy  

---

## 1. Object-Oriented Programming (OOP) in Modern Java

### A. Four Pillars in Production Context
1. **Encapsulation:** Hiding internal mutable state behind access boundaries.
   *Senior nuance:* In Java 21, `record` classes provide pure data carriers where all fields are `private final` with auto-generated accessors.
2. **Abstraction:** Exposing contracts (`interface`) while concealing concrete mechanics.
   *Senior nuance:* Prefer composition over inheritance. In modern Java, interfaces can supply default and static methods, but they cannot hold mutable instance state.
3. **Inheritance:** "Is-A" relationship. Class inheritance is single-parent; interface implementation is multiple.
   *Pitfall:* Deep class hierarchies lead to the *fragile base class problem*, where modifications to a superclass inadvertently break subclasses.
4. **Polymorphism:** Compile-time (Overloading) vs. Runtime (Overriding).
   *Internals:* Runtime polymorphism uses the **vtable (Virtual Method Table)**. Every Java class has an array of method pointers. When `employee.calculateBonus()` is called, the JVM looks up the offset in the runtime object's vtable to dispatch to the overriding implementation.

### B. Composition vs. Inheritance (Effective Java Item 18)
- **Inheritance:** Breaks encapsulation if superclass exposes internal implementation assumptions.
- **Composition:** Wrap an instance of another class inside your class and delegate calls. Allows dynamic swapping of behavior at runtime (Strategy Pattern) without tight coupling.

---

## 2. Java Memory Model & Pass-by-Value Mechanics

### The Golden Rule: **Java is strictly 100% Pass-by-Value.**
There is **no pass-by-reference in Java**.

```text
Stack (Thread-Local)                      Heap (Shared)
┌───────────────────────┐                 ┌───────────────────────┐
│ main() frame:         │                 │                       │
│  int a = 10;          │                 │                       │
│  Person p1 = 0x1A2B ──┼────────────────▶│ 0x1A2B:               │
└───────────────────────┘                 │   Person {            │
                                          │     name = "Asha"     │
┌───────────────────────┐                 │   }                   │
│ modify(Person p) frame│                 │                       │
│  p = 0x1A2B (COPY) ───┼─────────────────┘                       │
│  p.name = "Vikram" ───┼────────────────▶ (Mutates Heap object)   │
│  p = new Person(...) ─┼────────────────▶ 0x3C4D (Reassigns local│
└───────────────────────┘                  copy only!)            │
```

- When passing an `int`, a copy of the primitive value is pushed onto the callee's stack frame.
- When passing an object reference `Person p`, a **copy of the 64-bit memory address pointer** is pushed onto the callee's stack frame.
- Modifying fields via `p.setName("Vikram")` mutates the underlying heap object because both references point to the same address.
- Reassigning `p = new Person("Rohan")` only alters the local stack variable copy in the callee frame; the caller's reference remains unchanged!

---

## 3. String Constant Pool & String Mutability

### Memory Architecture
- Strings are immutable character arrays (`byte[]` in Java 9+ with compact strings).
- **String Constant Pool (SCP):** A dedicated hash table (`StringTable`) located in the **Java Heap** (moved out of PermGen in Java 7).
- String literals (`String s = "Java"`) are checked against the pool:
  - If already present, the existing heap reference is returned.
  - If absent, a new string is created in the pool and returned.
- `new String("Java")` creates **two objects**: one in the general Heap and one in the String Constant Pool (if not already present).
- `s.intern()` places or retrieves the canonical reference from the SCP.

### String vs. StringBuilder vs. StringBuffer

| Feature | `String` | `StringBuilder` | `StringBuffer` |
|---|---|---|---|
| **Mutability** | Immutable | Mutable | Mutable |
| **Thread Safety** | Thread-safe (read-only) | Not thread-safe | Thread-safe (methods synchronized) |
| **Performance** | High concatenation overhead ($O(n^2)$) | Fastest ($O(1)$ amortized append) | Slower due to lock acquisition overhead |
| **Memory** | New object per modification | Internal resizable `byte[]` | Internal resizable `byte[]` |

---

## 4. The `equals()` and `hashCode()` Contract (Mandatory)

### Formal Rules (JLS):
1. **Reflexive:** `x.equals(x)` must be `true`.
2. **Symmetric:** `x.equals(y)` must return `true` if and only if `y.equals(x)` returns `true`.
3. **Transitive:** If `x.equals(y)` and `y.equals(z)`, then `x.equals(z)` must return `true`.
4. **Consistent:** Repeated invocations must return the same result provided fields are unchanged.
5. **Non-nullity:** `x.equals(null)` must return `false`.
6. **The HashCode Rule:** **If two objects are equal according to `equals()`, they MUST produce the exact same `hashCode()`.**

### What Happens If You Violate This Rule?
If you override `equals()` but fail to override `hashCode()`:
- Two logically equal objects will produce different hash codes.
- Storing them in a `HashSet` or as keys in a `HashMap` will place them into different buckets.
- `map.get(new Key("Asha"))` will compute the wrong bucket index and return `null`, even though the exact logical key was added previously!

---

## 5. Generics & The PECS Principle

### Producer Extends, Consumer Super (PECS)
- **Use `? extends T` (Covariance) when you only READ from the collection:**
  ```java
  public double sumOfSalaries(List<? extends Employee> employees) {
      double total = 0;
      for (Employee e : employees) { // Safe to read as Employee
          total += e.getSalary().doubleValue();
      }
      // employees.add(new Manager()); // COMPILE ERROR: Cannot add!
      return total;
  }
  ```
- **Use `? super T` (Contravariance) when you only WRITE into the collection:**
  ```java
  public void addEngineers(List<? super Engineer> list) {
      list.add(new Engineer("Asha")); // Safe to write
      // Engineer e = list.get(0); // COMPILE ERROR: Can only read as Object!
  }
  ```

---

## 6. High-Frequency Core Java Interview Questions

### Q1: Why is `String` immutable in Java?
**Answer:**
1. **Security:** Strings represent file paths, network URLs, database usernames, and passwords. Mutability would allow malicious code to alter connection destinations after security checks.
2. **Thread Safety:** Immutable objects can be safely shared across concurrent threads without locks.
3. **Caching & HashCode:** Strings cache their `hashCode` upon first computation. If mutable, hash table lookups would fail after field modification.
4. **String Constant Pool:** Pool sharing requires immutability; otherwise altering one reference would mutate all other references sharing the literal.

### Q2: What is the difference between `final`, `finally`, and `finalize`?
**Answer:**
- `final`: Keyword applying to classes (prevents inheritance), methods (prevents overriding), and variables (prevents reassignment).
- `finally`: Block in exception handling that executes regardless of whether an exception was thrown or caught (used for resource cleanup, superseded by `try-with-resources`).
- `finalize`: Deprecated in Java 9, removed in modern Java. Legacy method called by Garbage Collector before reclaiming an object. Unpredictable, hazardous, and should never be used.

### Q3: Explain Checked vs. Unchecked Exceptions and when to use which.
**Answer:**
- **Checked (`Exception` subclasses excluding `RuntimeException`):** Enforced by compiler; caller must catch or declare (`throws`). Use for recoverable conditions where the client can reasonably take alternative action (e.g., `FileNotFoundException`).
- **Unchecked (`RuntimeException` and its subclasses):** Not enforced at compile time. Represents programming defects or unrecoverable client errors (e.g., `NullPointerException`, `IllegalArgumentException`, `ResourceNotFoundException`). Modern frameworks (Spring) exclusively prefer unchecked exceptions.

### Q4: How does a Java `record` differ from a traditional Class?
**Answer:**
A `record` (Java 16+) is an immutable, transparent carrier for data:
- All fields are implicitly `private final`.
- The record class is implicitly `final` (cannot be extended).
- Automatically generates canonical constructor, getters (without `get` prefix), `equals()`, `hashCode()`, and `toString()`.
- Does not allow custom instance fields (only static fields).
