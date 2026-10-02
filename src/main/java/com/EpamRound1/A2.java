package com.dipdeveloper;

// Before Java 8 - Anonymous Inner Class
Comparator<Integer> comparator = new Comparator<Integer>() {
    @Override
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }
};

// After Java 8 - Lambda Expression
Comparator<Integer> comparator = (a, b) -> a.compareTo(b);


//---------------------

@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);

    // You CAN have default and static methods
    default void print() {
        System.out.println("Calculating...");
    }
}

// Using it with Lambda
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.calculate(5, 3));        // Output: 8
        System.out.println(multiply.calculate(5, 3));  // Output: 15