package com.dipdeveloper;

import java.util.Stack;

class SmallestNumberAfterRemovingK {

    public static String removeKdigits(String num, int k) {
        // Stack to maintain monotonic increasing sequence
        Stack<Character> stack = new Stack<>();

        // Step 1: Process each digit
        for (char digit : num.toCharArray()) {
            // Remove larger digits from stack when we find smaller digit
            while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }

        // Step 2: If k is still > 0, remove from end
        while (k > 0) {
            stack.pop();
            k--;
        }

        // Step 3: Build result
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        result.reverse();

        // Step 4: Remove leading zeros
        String res = result.toString();
        int index = 0;
        while (index < res.length() - 1 && res.charAt(index) == '0') {
            index++;
        }

        return res.substring(index);
    }

    public static void main(String[] args) {
        System.out.println(removeKdigits("1432219", 3));  // Output: "1219"
        System.out.println(removeKdigits("10200", 1));    // Output: "200"
        System.out.println(removeKdigits("123456", 2));   // Output: "1234"
    }
}
