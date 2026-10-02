package com.dipdeveloper;

// VOLATILE - Just visibility, no atomicity
public class VolatileExample {
    private volatile boolean flag = false;

    // Thread 1
    public void setFlag() {
        flag = true;  // This write is immediately visible to other threads
    }

    // Thread 2
    public void checkFlag() {
        while (!flag) {  // Will see the update immediately
            // keep waiting
        }
        System.out.println("Flag is now true!");
    }
}

// SYNCHRONIZED - Mutual exclusion + visibility
public class SynchronizedExample {
    private int counter = 0;

    public synchronized void increment() {
        counter++;  // Only one thread can execute this at a time
    }

    public synchronized int getCounter() {
        return counter;
    }
}

// When to use what:
// 1. Use volatile for simple flags or status variables
// 2. Use synchronized for shared objects with multiple operations

// Real-world example: Double-checked locking
public class Singleton {
    private static volatile Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {  // First check (no lock, fast)
            synchronized (Singleton.class) {
                if (instance == null) {  // Second check (under lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
