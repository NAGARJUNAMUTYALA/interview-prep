package com.example.interviewprep.practice.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Collections Framework Internals & Concurrency")
class CollectionsInternalsTest {

    // ─── 1. HashMap Internals: Bitwise Index & Collisions ────────────────────────

    static class BadHashKey {
        final String value;
        BadHashKey(String value) { this.value = value; }

        @Override
        public int hashCode() {
            return 42; // Intentionally force all instances into the exact same bucket
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BadHashKey that)) return false;
            return Objects.equals(value, that.value);
        }
    }

    @Test
    @DisplayName("HashMap collision handling: different keys with identical hashCode")
    void testHashMapCollisionResolution() {
        Map<BadHashKey, String> map = new HashMap<>();
        BadHashKey k1 = new BadHashKey("Key1");
        BadHashKey k2 = new BadHashKey("Key2");
        BadHashKey k3 = new BadHashKey("Key3");

        map.put(k1, "Val1");
        map.put(k2, "Val2");
        map.put(k3, "Val3");

        assertEquals(3, map.size());
        assertEquals("Val1", map.get(k1));
        assertEquals("Val2", map.get(k2));
        assertEquals("Val3", map.get(k3));
    }

    @Test
    @DisplayName("HashMap bucket index calculation: (n - 1) & hash")
    void testBucketIndexCalculation() {
        int tableCapacity = 16;
        int hash = 1234567;
        int index = (tableCapacity - 1) & hash;

        assertTrue(index >= 0 && index < tableCapacity, "Bucket index must strictly lie within [0, capacity - 1]");
        assertEquals(hash % tableCapacity, index, "Bitwise AND equals modulo operator when capacity is a power of 2");
    }

    // ─── 2. ConcurrentHashMap vs HashMap Concurrency ────────────────────────────

    @Test
    @DisplayName("ConcurrentHashMap guarantees safe concurrent mutations")
    void testConcurrentHashMapThreadSafety() throws InterruptedException {
        int threadCount = 10;
        int operationsPerThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        concurrentMap.put("Key-" + threadId + "-" + j, j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(threadCount * operationsPerThread, concurrentMap.size(),
                "All concurrent entries must be recorded without lost updates or race conditions");
    }

    // ─── 3. PriorityQueue (Min-Heap / Max-Heap) ──────────────────────────────────

    @Test
    @DisplayName("PriorityQueue min-heap and custom max-heap ordering")
    void testPriorityQueueOrdering() {
        // Min-Heap (default natural order)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.addAll(List.of(45, 10, 80, 5, 25));

        assertEquals(5, minHeap.poll());
        assertEquals(10, minHeap.poll());

        // Max-Heap (reverse order)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(List.of(45, 10, 80, 5, 25));

        assertEquals(80, maxHeap.poll());
        assertEquals(45, maxHeap.poll());
    }
}
