# JVM Internals, Memory Architecture & Production Troubleshooting

**Target Audience:** Mid-to-Senior Java Developers (~5 Years Experience)  
**Topic:** Heap, Metaspace, GC mechanics, diagnosing OOM, and memory leak analysis  

---

## 1. JVM Runtime Data Areas

```text
┌─────────────────────────────────────────────────────────────┐
│                       JVM Process                           │
│                                                             │
│  ┌─────────────────────── Heap Memory ──────────────────┐   │
│  │  Young Generation                Old Generation      │   │
│  │  ┌────────────┬──────┬──────┐    ┌────────────────┐  │   │
│  │  │    Eden    │  S0  │  S1  │    │    Tenured     │  │   │
│  │  └────────────┴──────┴──────┘    └────────────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌──────── Non-Heap ──────┐    ┌───── Thread Specific ──┐   │
│  │       Metaspace        │    │  Thread 1   Thread 2   │   │
│  │ (Class metadata,       │    │  ┌───────┐  ┌───────┐  │   │
│  │  JIT compiled code)    │    │  │ Stack │  │ Stack │  │   │
│  │ (Native OS Memory)     │    │  └───────┘  └───────┘  │   │
│  └────────────────────────┘    └────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Generational Hypothesis:
*Most allocated objects die young.*
1. **Eden Space:** New objects allocated here.
2. **Survivor Spaces (S0 / S1):** Objects surviving Minor GC are copied back and forth between S0 and S1. Each survival increments the object's age counter (stored in 4 bits of the Object Header Mark Word, max age 15).
3. **Tenured Space (Old Gen):** Objects reaching tenure threshold (default 15 in G1GC) are promoted here.
4. **Metaspace:** Stored in native memory (replaces PermGen in Java 8+). Holds class definitions, method bytecode, annotations, and constant pools.

---

## 2. Production Garbage Collectors Comparison

| Garbage Collector | Target Workload | Pause Characteristics | Key Mechanism |
|---|---|---|---|
| **G1GC** (Default in Java 9–21) | Balanced throughput & low latency | Configurable target pause (e.g. `-XX:MaxGCPauseMillis=200`) | Partitions heap into 2048 regions; collects regions with most garbage first ("Garbage-First"). |
| **ZGC** (Generational ZGC in Java 21) | Ultra-low latency (< 1ms pauses) for large heaps (up to 16TB) | Sub-millisecond pause times | Uses colored pointers and load barriers; performs almost all phases concurrently with application threads. |
| **Parallel GC** | Batch processing, ETL, offline jobs | Longer pauses, maximum throughput | Stop-the-world multithreaded collection maximizing CPU time for application. |

---

## 3. Production Troubleshooting Toolkit

### Essential CLI Commands
```bash
# 1. Identify Java Process ID (PID)
jcmd

# 2. Check live Heap Usage and summary
jcmd <PID> GC.heap_info

# 3. Print Top 20 objects consuming memory on the Heap (histogram)
jcmd <PID> GC.class_histogram | head -n 25

# 4. Generate Heap Dump without crashing application
jcmd <PID> GC.dump /tmp/heapdump_$(date +%s).hprof

# 5. Capture Thread Dump to diagnose deadlocks or high CPU
jcmd <PID> Thread.print > /tmp/threaddump_$(date +%s).txt

# 6. Check JVM uptime and launch flags
jcmd <PID> VM.flags
```

### Top Production JVM Flags
```bash
-Xms2g -Xmx2g \
-XX:+UseG1GC \
-XX:MaxGCPauseMillis=200 \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/var/log/heapdump.hprof \
-Xlog:gc*,gc+age=trace:file=/var/log/gc.log:time,uptime,pid:filecount=5,filesize=50m
```
