// BOXING (Inbound) - Primitive to Object
int primitiveInt = 5;
Integer boxedInt = primitiveInt;  // Auto-boxing
System.out.println(boxedInt);      // Output: 5

// UNBOXING (Outbound) - Object to Primitive
Integer integerObject = 10;
int unboxedInt = integerObject;    // Auto-unboxing
System.out.println(unboxedInt);    // Output: 10


// Before Java 5 (Manual boxing/unboxing)
Integer manualBox = Integer.valueOf(5);
int manualUnbox = manualBox.intValue();


// Common pitfall: Null Pointer Exception during unboxing
Integer nullable = null;
int unboxThis = nullable;  // NullPointerException!

// Safe approach:
Integer nullable2 = null;
int value = nullable2 != null ? nullable2 : 0;  // Safe


// Performance consideration:
// Use primitive types in loops for better performance
// Collections require objects, so use wrapper types there

List<Integer> numbers = Arrays.asList(1, 2, 3);  // Auto-boxing each
for (Integer num : numbers) {
int primitive = num;  // Auto-unboxing
}