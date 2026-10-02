package com.dipdeveloper;

import java.util.*;
import java.util.stream.Collectors;

class StreamsTopElements {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(45, 23, 67, 89, 12, 90, 34, 56, 78, 11);

        // ═══════════════════════════════════════════════════
        // APPROACH 1: Using sorted() with limit()
        // ═══════════════════════════════════════════════════

        System.out.println("=== Top 3 Maximum Elements ===");
        List<Integer> top3Max = numbers.stream()
                .sorted(Collections.reverseOrder())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(top3Max);  // Output: [90, 89, 78]

        System.out.println("\n=== Top 3 Minimum Elements ===");
        List<Integer> top3Min = numbers.stream()
                .sorted()
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(top3Min);  // Output: [11, 12, 23]

        // ═══════════════════════════════════════════════════
        // APPROACH 2: Using distinct() - if duplicates exist
        // ═══════════════════════════════════════════════════

        List<Integer> numbersWithDuplicates = Arrays.asList(45, 23, 67, 89, 45, 90, 34, 23, 78, 11);

        System.out.println("\n=== Top 3 Max (Distinct) ===");
        List<Integer> top3MaxDistinct = numbersWithDuplicates.stream()
                .distinct()
                .sorted(Collections.reverseOrder())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(top3MaxDistinct);

        // ═══════════════════════════════════════════════════
        // APPROACH 3: Using TreeMap (Efficient for small K)
        // ═══════════════════════════════════════════════════

        System.out.println("\n=== Top 3 Max using TreeMap (Most Efficient) ===");
        numbers.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Collections.reverseOrder())))
                .stream()
                .limit(3)
                .forEach(System.out::println);

        // ═══════════════════════════════════════════════════
        // APPROACH 4: Using reduce() - For Custom Objects
        // ═══════════════════════════════════════════════════

        System.out.println("\n=== Using Map for Complex Objects ===");
        List<Employee> employees = Arrays.asList(
                new Employee("John", 50000),
                new Employee("Alice", 75000),
                new Employee("Bob", 60000),
                new Employee("Charlie", 90000),
                new Employee("David", 55000)
        );

        List<Employee> top3HighestSalary = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(3)
                .collect(Collectors.toList());

        System.out.println("Top 3 Highest Salary:");
        top3HighestSalary.forEach(emp ->
                System.out.println(emp.getName() + " - $" + emp.getSalary())
        );
    }

    // Helper class
    static class Employee {
        String name;
        double salary;

        Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() { return name; }
        public double getSalary() { return salary; }
    }
}
