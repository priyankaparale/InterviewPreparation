package com.dipdeveloper;

import java.util.*;

class PriorityQueueExample {

    public static void main(String[] args) {

        // ═══════════════════════════════════════════════════
        // MIN HEAP (Default)
        // ═══════════════════════════════════════════════════
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        minHeap.add(5);
        minHeap.add(2);
        minHeap.add(8);
        minHeap.add(1);
        minHeap.add(9);

        System.out.println("Min Heap polling:");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");  // Output: 1 2 5 8 9
        }


        // ═══════════════════════════════════════════════════
        // MAX HEAP (Custom)
        // ═══════════════════════════════════════════════════
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
                Collections.reverseOrder()
        );

        maxHeap.add(5);
        maxHeap.add(2);
        maxHeap.add(8);
        maxHeap.add(1);
        maxHeap.add(9);

        System.out.println("\n\nMax Heap polling:");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");  // Output: 9 8 5 2 1
        }


        // ═══════════════════════════════════════════════════
        // CUSTOM PRIORITY (Task Scheduling)
        // ═══════════════════════════════════════════════════
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
                (t1, t2) -> Integer.compare(t1.priority, t2.priority)
        );

        taskQueue.add(new Task("Email", 2));
        taskQueue.add(new Task("Bug Fix", 1));
        taskQueue.add(new Task("Feature", 3));

        System.out.println("\n\nTask Priority Queue:");
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println(task);
        }
    }

    static class Task {
        String name;
        int priority;

        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "[" + priority + "] " + name;
        }
    }
}