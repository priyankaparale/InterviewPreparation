package com.dipdeveloper;

// ═══════════════════════════════════════════════════
// ArrayList - Fast access, slow insertion
// ═══════════════════════════════════════════════════

List<Integer> arrayList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// Fast: O(1)
int element = arrayList.get(2);  // 3

// Slow: O(n) - requires shifting
arrayList.add(1, 99);  // Insert 99 at index 1

// NOT thread-safe
// To make it thread-safe:
List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());


// ═══════════════════════════════════════════════════
// LinkedList - Slow access, fast insertion at ends
// ═══════════════════════════════════════════════════

LinkedList<Integer> linkedList = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));

// Slow: O(n)
int element = linkedList.get(2);  // 3

// Fast: O(1)
linkedList.addFirst(99);   // Add at beginning
linkedList.addLast(100);   // Add at end

// Iterator for LinkedList is more efficient than index-based
for (int val : linkedList) {
        System.out.println(val);
}


// ═══════════════════════════════════════════════════
// ConcurrentHashMap - Thread-safe without full lock
// ═══════════════════════════════════════════════════

ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Multiple threads can work simultaneously (different buckets)
map.put("key1", 100);
map.put("key2", 200);

// Safe to iterate while other threads are modifying
for (Map.Entry<String, Integer> entry : map.entrySet()) {
        System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

// Compare with Hashtable (fully synchronized, slower)
Hashtable<String, Integer> hashtable = new Hashtable<>();
hashtable.put("key1", 100);
// Entire table is locked for any operation


// ═══════════════════════════════════════════════════
// Performance Comparison
// ═══════════════════════════════════════════════════

public class PerformanceComparison {
    public static void main(String[] args) {

        // ArrayList access performance
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            arrayList.add(i);
        }

        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            arrayList.get(50000);  // Fast
        }
        System.out.println("ArrayList get: " + (System.nanoTime() - start) + " ns");


        // LinkedList access performance
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 100000; i++) {
            linkedList.add(i);
        }

        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            linkedList.get(50000);  // Slow - traverses from beginning
        }
        System.out.println("LinkedList get: " + (System.nanoTime() - start) + " ns");
    }
}
