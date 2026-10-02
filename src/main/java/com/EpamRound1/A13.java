package com.dipdeveloper;

import java.util.*;
import java.util.concurrent.*;

class ExecutorServiceExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // ═══════════════════════════════════════════════════
        // FIXED THREAD POOL
        // ═══════════════════════════════════════════════════
        System.out.println("=== Fixed Thread Pool ===");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " running on " +
                        Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();  // No new tasks accepted
        executor.awaitTermination(5, TimeUnit.SECONDS);  // Wait for completion


        // ═══════════════════════════════════════════════════
        // CALLABLE & FUTURE (Getting Results)
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== Callable with Future ===");
        ExecutorService executor2 = Executors.newFixedThreadPool(2);

        // Submit callable task
        Future<Integer> future = executor2.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Task running...");
                Thread.sleep(2000);
                return 42;  // Return a result
            }
        });

        System.out.println("Waiting for result...");
        Integer result = future.get();  // Blocks until result is ready
        System.out.println("Result: " + result);

        executor2.shutdown();


        // ═══════════════════════════════════════════════════
        // SCHEDULED EXECUTOR
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== Scheduled Executor ===");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Run once after delay
        scheduler.schedule(() -> {
            System.out.println("Delayed task executed!");
        }, 2, TimeUnit.SECONDS);

        // Run repeatedly at fixed rate
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Repeating task at " + new Date());
        }, 0, 3, TimeUnit.SECONDS);

        Thread.sleep(10000);  // Let it run for a bit
        scheduler.shutdown();


        // ═══════════════════════════════════════════════════
        // SUBMIT MULTIPLE AND WAIT
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== invokeAll (Batch Processing) ===");
        ExecutorService executor3 = Executors.newFixedThreadPool(3);

        List<Callable<String>> tasks = Arrays.asList(
                () -> { Thread.sleep(1000); return "Task 1"; },
                () -> { Thread.sleep(2000); return "Task 2"; },
                () -> { Thread.sleep(500);  return "Task 3"; }
        );

        List<Future<String>> futures = executor3.invokeAll(tasks);

        for (Future<String> f : futures) {
            System.out.println(f.get());
        }

        executor3.shutdown();
    }
}
