package com.example.interviewprep.practice.concurrency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Concurrency, Multithreading & Virtual Threads Practice")
class ConcurrencyPracticeTest {

    // ─── 1. CompletableFuture Async Pipeline ────────────────────────────────────

    @Test
    @DisplayName("CompletableFuture chaining: supplyAsync, thenApply, thenCombine, exceptionally")
    void testCompletableFuturePipeline() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> userTask = CompletableFuture.supplyAsync(() -> {
            return "User:Asha";
        }, executor);

        CompletableFuture<Double> salaryTask = CompletableFuture.supplyAsync(() -> {
            return 85000.00;
        }, executor);

        CompletableFuture<String> combinedTask = userTask.thenCombine(salaryTask, (user, salary) -> {
            return user + " earns $" + salary;
        }).exceptionally(ex -> "Fallback: Failed to fetch");

        String result = combinedTask.get(2, TimeUnit.SECONDS);
        assertEquals("User:Asha earns $85000.0", result);

        executor.shutdown();
    }

    // ─── 2. Producer-Consumer with ArrayBlockingQueue ────────────────────────────

    @Test
    @DisplayName("Producer-Consumer pattern using ArrayBlockingQueue")
    void testProducerConsumer() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        AtomicInteger consumedSum = new AtomicInteger(0);
        int totalItems = 10;

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= totalItems; i++) {
                try {
                    queue.put(i); // Blocks if full
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= totalItems; i++) {
                try {
                    Integer val = queue.take(); // Blocks if empty
                    consumedSum.addAndGet(val);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();

        producer.join(3000);
        consumer.join(3000);

        assertEquals(55, consumedSum.get(), "Sum of numbers 1..10 must be 55");
    }

    // ─── 3. Java 21 Virtual Threads ─────────────────────────────────────────────

    @Test
    @DisplayName("Java 21 Virtual Threads: lightweight thread execution")
    void testVirtualThreads() throws InterruptedException {
        int taskCount = 1000;
        AtomicInteger counter = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    assertTrue(Thread.currentThread().isVirtual(), "Thread must be an active Java 21 Virtual Thread");
                    counter.incrementAndGet();
                });
            }
        } // Implicitly awaits termination of virtual threads on try-with-resources close

        assertEquals(taskCount, counter.get(), "All 1000 virtual tasks must complete successfully");
    }
}
