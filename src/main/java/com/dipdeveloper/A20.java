package com.dipdeveloper;

class LargestPalindromeSubstring {

    // ═══════════════════════════════════════════════════
    // APPROACH 1: Brute Force O(n^3)
    // ═══════════════════════════════════════════════════
    public static String longestPalindromeBruteForce(String s) {
        if (s == null || s.length() < 1) return "";

        String longest = "";

        // Check every substring
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                String substring = s.substring(i, j + 1);
                if (isPalindrome(substring)) {
                    if (substring.length() > longest.length()) {
                        longest = substring;
                    }
                }
            }
        }

        return longest;
    }

    private static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }


    // ═══════════════════════════════════════════════════
    // APPROACH 2: Expand Around Center O(n^2)
    // ═══════════════════════════════════════════════════
    public static String longestPalindromeExpand(String s) {
        if (s == null || s.length() < 1) return "";

        int maxLen = 0;
        int start = 0;

        // Try each position as center
        for (int i = 0; i < s.length(); i++) {
            // Odd-length palindromes (single character center)
            int len1 = expandAroundCenter(s, i, i);

            // Even-length palindromes (two character center)
            int len2 = expandAroundCenter(s, i, i + 1);

            int len = Math.max(len1, len2);

            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2;
            }
        }

        return s.substring(start, start + maxLen);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;  // Length of palindrome
    }


    // ═══════════════════════════════════════════════════
    // APPROACH 3: Dynamic Programming O(n^2)
    // ═══════════════════════════════════════════════════
    public static String longestPalindromeDP(String s) {
        if (s == null || s.length() < 1) return "";

        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int maxLen = 1;
        int start = 0;

        // Every single character is a palindrome
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // Check for 2-character palindromes
        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                maxLen = 2;
                start = i;
            }
        }

        // Check for palindromes of length 3 or more
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1;

                // If characters match and inner string is palindrome
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    maxLen = len;
                    start = i;
                }
            }
        }

        return s.substring(start, start + maxLen);
    }


    public static void main(String[] args) {
        String[] testCases = {
                "abacabad",
                "racecar",
                "12345",
                "a",
                "ac",
                "abcdcba"
        };

        System.out.println("=== Brute Force O(n^3) ===");
        for (String test : testCases) {
            System.out.println("Input: " + test + " -> " + longestPalindromeBruteForce(test));
        }

        System.out.println("\n=== Expand Around Center O(n^2) ===");
        for (String test : testCases) {
            System.out.println("Input: " + test + " -> " + longestPalindromeExpand(test));
        }

        System.out.println("\n=== Dynamic Programming O(n^2) ===");
        for (String test : testCases) {
            System.out.println("Input: " + test + " -> " + longestPalindromeDP(test));
        }
    }
}
