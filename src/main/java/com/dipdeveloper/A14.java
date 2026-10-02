package com.dipdeveloper;

import java.util.*;

class ComparatorExample {

    static class Employee {
        String name;
        double salary;
        int yearsOfExperience;

        Employee(String name, double salary, int years) {
            this.name = name;
            this.salary = salary;
            this.yearsOfExperience = years;
        }
        private static double getSalary(Employee e) { return e.salary; }
        private static int getYearsOfExperience(Employee e) { return e.yearsOfExperience; }
        @Override
        public String toString() {
            return name + " ($" + salary + ", " + yearsOfExperience + " yrs)";
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", 50000, 3),
                new Employee("Bob", 75000, 5),
                new Employee("Charlie", 60000, 2),
                new Employee("David", 90000, 8)
        );

        // ═══════════════════════════════════════════════════
        // SORT BY SALARY (Ascending)
        // ═══════════════════════════════════════════════════
        employees.sort(Comparator.comparingDouble(Employee::getSalary));
        System.out.println("Sorted by salary (ascending):");
        employees.forEach(System.out::println);


        // ═══════════════════════════════════════════════════
        // SORT BY SALARY (Descending)
        // ═══════════════════════════════════════════════════
        employees.sort(Comparator.comparingDouble(Employee::getSalary).reversed());
        System.out.println("\nSorted by salary (descending):");
        employees.forEach(System.out::println);


        // ═══════════════════════════════════════════════════
        // SORT BY MULTIPLE CRITERIA
        // ═══════════════════════════════════════════════════
        employees.sort(
                Comparator.comparingInt(Employee::getYearsOfExperience).reversed()
                        .thenComparingDouble(Employee::getSalary).reversed()
        );
        System.out.println("\nSorted by experience (desc), then salary (desc):");
        employees.forEach(System.out::println);


        // ═══════════════════════════════════════════════════
        // CUSTOM COMPARATOR (Anonymous Class)
        // ═══════════════════════════════════════════════════
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return e1.name.compareTo(e2.name);  // Alphabetical order
            }
        });
        System.out.println("\nSorted by name (alphabetical):");
        employees.forEach(System.out::println);


        // ═══════════════════════════════════════════════════
        // LAMBDA EXPRESSION
        // ═══════════════════════════════════════════════════
        employees.sort((e1, e2) -> e1.name.compareTo(e2.name));
        System.out.println("\nSorted by name (using lambda):");
        employees.forEach(System.out::println);
    }


}
