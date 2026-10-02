package com.dipdeveloper;

class GarbageCollectionExample {

    static class LargeObject {
        byte[] data = new byte[10 * 1024 * 1024];  // 10 MB

        @Override
        protected void finalize() throws Throwable {
            System.out.println("Object is being garbage collected");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Memory Information ===");
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
        System.out.println("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + " MB");
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");


        // ═══════════════════════════════════════════════════
        // TRIGGER GARBAGE COLLECTION
        // ═══════════════════════════════════════════════════

        System.out.println("\n=== Creating Objects ===");
        LargeObject obj1 = new LargeObject();
        System.out.println("Free Memory after creating object: " +
                runtime.freeMemory() / 1024 / 1024 + " MB");

        // Dereference (eligible for GC)
        obj1 = null;

        System.out.println("\n=== Requesting Garbage Collection ===");
        System.gc();  // Request GC (not guaranteed)
        Thread.sleep(1000);

        System.out.println("Free Memory after GC: " +
                runtime.freeMemory() / 1024 / 1024 + " MB");


        // ═══════════════════════════════════════════════════
        // JVM FLAGS FOR GC TUNING
        // ═══════════════════════════════════════════════════

        /*
        Run with these JVM flags:

        -Xms2G           : Initial heap size
        -Xmx4G           : Maximum heap size
        -XX:+UseG1GC     : Use G1 Garbage Collector
        -XX:+PrintGCDetails : Print detailed GC logs
        -XX:+PrintGCTimeStamps : Print GC timestamps

        Example:
        java -Xms2G -Xmx4G -XX:+UseG1GC MyApp
        */

        System.out.println("\nFor optimal GC performance:");
        System.out.println("-Xms: Set initial heap size");
        System.out.println("-Xmx: Set maximum heap size");
        System.out.println("-XX:+UseG1GC: Use G1 Garbage Collector");
    }
}
