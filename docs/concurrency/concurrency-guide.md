# Java Concurrency, Multithreading & Memory Model Guide

**Target Audience:** Senior Java Backend Engineers (~5 Years Experience)  
**Key Focus:** Production multithreading, concurrency pitfalls, and architecture questions  

---

## 1. Java Memory Model (JMM) & The Happens-Before Relationship

The JVM runs on modern multi-core CPUs where each core has its own L1/L2/L3 hardware caches.

```text
Core 1 (CPU) ──▶ L1/L2 Cache (Local copy of X) ──┐
                                                 ▼
                                            Main Memory (Shared X)
                                                 ▲
Core 2 (CPU) ──▶ L1/L2 Cache (Local copy of X) ──┘
```

Without synchronization:
1. **Visibility Issue:** Updates made by Core 1 remain in its L1/L2 cache and are invisible to Core 2.
2. **Instruction Reordering:** Compilers and CPUs reorder instructions for performance as long as single-threaded behavior (as-if-serial) is preserved.

### The `volatile` Keyword
- **Visibility Guarantee:** Any write to a `volatile` variable is flushed immediately to Main Memory. Any read of a `volatile` variable bypasses CPU caches and reads directly from Main Memory.
- **Happens-Before Guarantee:** A write to a `volatile` variable *happens-before* every subsequent read of that same variable.
- **Instruction Reordering Barrier:** The compiler generates hardware memory fences (e.g. `mfence` on x86) preventing writes before the volatile store from being moved after it.
- **Caveat:** `volatile` does **NOT** guarantee atomicity! Compound operations like `count++` (`read -> modify -> write`) are NOT thread-safe with `volatile`. Use `AtomicInteger` or `LongAdder`.

---

## 2. Thread Pool Sizing Formula for Production

A frequent senior interview question: *"How do you size your thread pool for an API service?"*

### Subetzky-Goetz Formula:
$$N_{\text{threads}} = N_{\text{cpu}} \times U_{\text{cpu}} \times \left(1 + \frac{W}{C}\right)$$

Where:
- $N_{\text{cpu}}$: Number of available CPU cores (`Runtime.getRuntime().availableProcessors()`).
- $U_{\text{cpu}}$: Target CPU utilization ($0 \le U_{\text{cpu}} \le 1$, e.g., 0.8 for 80%).
- $W$: Wait time (time spent waiting for database queries, external REST APIs, disk I/O).
- $C$: Compute time (time spent executing CPU instructions, JSON parsing, calculations).

### Rule of Thumb:
- **CPU-Bound Tasks (Hashing, Crypto, Heavy Math):** $W/C \approx 0$.
  $$\text{Pool Size} = N_{\text{cpu}} + 1$$
  *(The $+1$ handles occasional page faults without leaving a core idle).*
- **I/O-Bound Tasks (REST APIs, DB Queries, Network calls):** $W \gg C$ (e.g. $W=100\text{ms}$, $C=5\text{ms} \implies W/C = 20$).
  $$\text{Pool Size} = 8 \times 0.8 \times (1 + 20) \approx 134 \text{ threads}$$

---

## 3. Java 21 Virtual Threads (Project Loom)

### Platform Threads vs. Virtual Threads
- **Platform Threads:** 1:1 mapping to Operating System (OS) kernel threads. Fixed 1MB stack size. Creating 10,000 platform threads consumes ~10GB of RAM and exhausts OS limits.
- **Virtual Threads:** $M:N$ mapping. Managed by the JVM runtime, stored on the Java Heap (a few hundred bytes). Hundreds of thousands of virtual threads can run concurrently.

### How Virtual Threads Work Internally:
1. Virtual threads run on a pool of standard OS **carrier threads** (ForkJoinPool).
2. When a virtual thread executes a blocking I/O operation (e.g. JDBC socket read, HTTP call), the JVM unmounts the virtual thread from the carrier thread and suspends its continuation on the Heap.
3. The carrier thread immediately executes another ready virtual thread!
4. When the I/O completes, the OS notifies the JVM, which remounts the virtual thread onto an available carrier thread to resume execution.

### The Virtual Thread "Pinning" Caveat (Interview Trap):
A virtual thread cannot unmount from its carrier thread if it is inside a `synchronized` block or calling native methods (JNI). This is called **thread pinning**.
*Best Practice:* Replace `synchronized` blocks with `ReentrantLock` in virtual thread environments!
