package com.dipdeveloper;

import java.util.*;

class GenericClassExample {

    // ═══════════════════════════════════════════════════
    // SIMPLE GENERIC CLASS
    // ═══════════════════════════════════════════════════
    static class Box<T> {
        private T value;

        public void put(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    // ═══════════════════════════════════════════════════
    // GENERIC WITH MULTIPLE TYPE PARAMETERS
    // ═══════════════════════════════════════════════════
    static class Pair<K, V> {
        private K key;
        private V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    // ═══════════════════════════════════════════════════
    // BOUNDED GENERIC (Must extend Number)
    // ═══════════════════════════════════════════════════
    static class Calculator<T extends Number> {
        public double add(T a, T b) {
            return a.doubleValue() + b.doubleValue();
        }
    }

    // ═══════════════════════════════════════════════════
    // GENERIC METHOD
    // ═══════════════════════════════════════════════════
    static class Repository {
        public static <T> T findFirst(List<T> list) {
            return list.isEmpty() ? null : list.get(0);
        }
    }

    public static void main(String[] args) {
        // ═══════════════════════════════════════════════════
        // USING SIMPLE GENERIC
        // ═══════════════════════════════════════════════════
        Box<String> stringBox = new Box<>();
        stringBox.put("Hello");
        System.out.println("String: " + stringBox.get());

        Box<Integer> intBox = new Box<>();
        intBox.put(42);
        System.out.println("Integer: " + intBox.get());


        // ═══════════════════════════════════════════════════
        // USING GENERIC WITH MULTIPLE TYPES
        // ═══════════════════════════════════════════════════
        Pair<String, Integer> pair = new Pair<>("age", 30);
        System.out.println("Key: " + pair.getKey() + ", Value: " + pair.getValue());


        // ═══════════════════════════════════════════════════
        // BOUNDED GENERIC
        // ═══════════════════════════════════════════════════
        Calculator<Integer> intCalc = new Calculator<>();
        System.out.println("10 + 20 = " + intCalc.add(10, 20));

        Calculator<Double> doubleCalc = new Calculator<>();
        System.out.println("10.5 + 20.5 = " + doubleCalc.add(10.5, 20.5));


        // ═══════════════════════════════════════════════════
        // GENERIC METHOD
        // ═══════════════════════════════════════════════════
        System.out.println("First int: " + Repository.findFirst(Arrays.asList(1, 2, 3)));
        System.out.println("First string: " + Repository.findFirst(Arrays.asList("a", "b")));
    }
}
