# Linux, Networking & Production Incident Debugging Guide

**Target Audience:** Senior Java Backend Engineers & Tech Leads  
**Key Focus:** Production incident response, system troubleshooting commands, and networking diagnostics  

---

## 1. Essential Linux Production Commands

### A. Process & Resource Inspection
```bash
# Real-time CPU & memory utilization per process
top -o %CPU
# Interactive htop (if installed)
htop

# Find all Java processes and arguments
ps aux | grep java

# Inspect memory consumption and swap
free -h

# Check disk space on all mounted filesystems
df -h

# Find directories consuming the most disk space
du -sh /* 2>/dev/null | sort -hr | head -n 10
```

### B. Network & Port Diagnostics
```bash
# Find process listening on specific port (e.g., 8081)
lsof -i :8081
# Linux socket statistics (replaces netstat)
ss -tulpn | grep 8081

# Check TCP connectivity to remote database
nc -zv postgres-host 5432

# DNS lookup and latency diagnosis
dig +trace db.internal.company.com
nslookup db.internal.company.com

# HTTP timing breakdown (DNS, TCP handshake, TLS, Time to First Byte)
curl -w "DNS: %{time_namelookup}s | Connect: %{time_connect}s | TTFB: %{time_starttransfer}s | Total: %{time_total}s\n" \
     -o /dev/null -s http://localhost:8081/api/v1/prepStart
```

---

## 2. Production Incident Runbook: High CPU Diagnostic Protocol

When a production alerting system (PagerDuty) notifies that a Java container has reached 100% CPU:

### Step 1: Find the High-CPU Native Thread ID
Run `top -H -p <PID>` to display individual threads inside the JVM process. Note the Thread ID (`TID`, e.g., `58421`) of the thread consuming the most CPU.

### Step 2: Convert Thread ID to Hexadecimal
```bash
printf '%x\n' 58421
# Output: e435
```

### Step 3: Capture Thread Dump and Match Hex ID
```bash
jcmd <PID> Thread.print > /tmp/threaddump.txt
grep -A 20 "nid=0xe435" /tmp/threaddump.txt
```
The resulting stack trace points to the **exact Java source file and line number** executing the CPU-spinning logic (e.g., infinite loop, unindexed collection scan, or regex catastrophic backtracking)!
