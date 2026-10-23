# Java HashMap Internals: In-Depth Architecture & Senior Interview Guide

**Target Audience:** Mid-to-Senior Java Developers (~5 Years Experience)  
**Topic Frequency:** Extremely High across technical coding and architecture rounds  

---

## 1. High-Level Architecture Overview

A `HashMap<K, V>` in Java is an array of buckets (`Node<K, V>[] table`) implementing the Map interface based on hashing.

```text
Node<K, V>[] table
  [0]  ──▶ null
  [1]  ──▶ Node("Asha", 95k) ──▶ Node("Vikram", 120k) ──▶ null  (Chaining via LinkedList)
  [2]  ──▶ null
  ...
  [8]  ──▶ TreeNode(Root) ──▶ TreeNodes (Red-Black Tree when bucket size >= 8)
  ...
  [15] ──▶ null
```

### Core Constants (JDK Source Code)
- `DEFAULT_INITIAL_CAPACITY`: `1 << 4` (16). Must always be a power of 2.
- `MAXIMUM_CAPACITY`: `1 << 30` (1,073,741,824).
- `DEFAULT_LOAD_FACTOR`: `0.75f`. The trade-off between time complexity and space overhead.
- `TREEIFY_THRESHOLD`: `8`. Threshold at which a bucket linked list is transformed into a balanced Red-Black tree.
- `UNTREEIFY_THRESHOLD`: `6`. Threshold at which a Red-Black tree is converted back into a linked list during resizing.
- `MIN_TREEIFY_CAPACITY`: `64`. Table capacity must be at least 64 before any bucket can treeify; otherwise, the table simply doubles in size.

---

## 2. Key Internal Mechanics

### A. Why Must Table Capacity Always Be a Power of Two?
In a hash table, index computation is mathematically:
$$\text{index} = \text{hash} \pmod{\text{capacity}}$$
However, integer modulo (`%`) requires CPU division cycles, which are computationally expensive.
When `capacity` is a power of 2 ($2^n$), the binary representation is `1` followed by $n$ zeroes. Subtracting 1 yields $n$ ones:
$$\text{capacity} = 16 \ (0001 \ 0000_2) \implies \text{capacity} - 1 = 15 \ (0000 \ 1111_2)$$

Therefore, index computation is performed using a single CPU clock cycle bitwise AND operation:
$$\text{index} = (\text{capacity} - 1) \ \& \ \text{hash}$$

### B. The Perturbation Function (Hash Spreading)
In Java 8+, `HashMap` applies a perturbation function:
```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
**Why XOR with `h >>> 16`?**
When capacity is small (e.g. 16), `(capacity - 1)` only uses the lowest 4 bits (`0000 1111`). If two distinct keys produce hash codes that differ only in the higher 16 bits, they will collide in the same bucket every single time!
By XOR-ing the higher 16 bits with the lower 16 bits (`h ^ (h >>> 16)`), variations from the upper bits are mixed into the lower bits, dramatically reducing collision probability.

### C. The `put(K, V)` Algorithm Step-by-Step
1. **Lazy Initialization:** If `table` is null or empty, `resize()` initializes the array with capacity 16.
2. **Hash & Index:** Compute perturbed `hash = hash(key)` and bucket index `i = (n - 1) & hash`.
3. **Empty Bucket:** If `table[i] == null`, allocate a new `Node(hash, key, value, null)` directly at `table[i]`.
4. **Collision Occurs (`table[i] != null`):**
   - Check if the head node matches: `if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))` ➔ Replace existing value.
   - Else if node is an instance of `TreeNode` ➔ Delegate to Red-Black tree insert (`putTreeVal`).
   - Else traverse the linked list:
     - Compare each node using `hash` and `equals()`. If match found, replace value.
     - If end of list reached without match, append new Node at tail.
     - Increment `binCount`. If `binCount >= TREEIFY_THRESHOLD (8)`:
       - If `table.length < MIN_TREEIFY_CAPACITY (64)`, invoke `resize()` (doubles table size).
       - If `table.length >= 64`, invoke `treeifyBin()` to convert linked list to a balanced Red-Black tree (search time drops from $O(n)$ to $O(\log n)$).
5. **Threshold Check:** Increment `size`. If `size > threshold (capacity * loadFactor)`, invoke `resize()`.

---

## 3. High-Frequency Senior Interview Q&A

### Q1: What happens if two distinct objects return the same `hashCode()`?
**Answer:** They collide and are placed into the same bucket. `HashMap` traverses the bucket's linked list or red-black tree, using `equals()` to find the exact match. If `equals()` returns false for all existing nodes, the new node is appended to the bucket.

### Q2: What happens if an object's `equals()` returns true, but `hashCode()` returns different values?
**Answer:** The contract is broken. `HashMap` will compute different bucket indices for the two identical objects. A `get(key)` call will search the wrong bucket and return `null`, resulting in silent data corruption.

### Q3: Why did Java 8 introduce Red-Black trees inside HashMap?
**Answer:** To defend against **Hash DoS (Denial of Service) attacks**. Malicious attackers can deliberately send HTTP requests with crafted payload strings designed to produce identical hash codes, collapsing a `HashMap` into a single linked list where lookups degrade to $O(n)$. With Red-Black trees, worst-case lookups are guaranteed at $O(\log n)$.

### Q4: Is `HashMap` thread-safe? What happens under concurrent mutations?
**Answer:** No. In Java 7, concurrent `resize()` could create a cyclic reference in the linked list, causing infinite loops and 100% CPU usage. In Java 8+, concurrent mutations cause lost updates or corrupt tree nodes. Use `ConcurrentHashMap` for multithreaded environments.

### Q5: How does `ConcurrentHashMap` achieve thread safety without locking the entire map?
**Answer:** In Java 8+, `ConcurrentHashMap` does **not** use table-level locks or segment locks. Instead:
- Reads are lock-free because Node `val` and `next` pointers are `volatile`.
- Inserts on empty buckets use lock-free **CAS (Compare-And-Swap)** on `table[i]`.
- Inserts on non-empty buckets use synchronized on only the **head node** of that specific bucket (`synchronized (f)`). Different threads writing to different buckets execute concurrently without blocking each other.
