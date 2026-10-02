package com.dipdeveloper;

import java.lang.reflect.*;

class ReflectionExample {

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void greet() {
            System.out.println("Hello, I'm " + name);
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    public static void main(String[] args) throws Exception {

        // ═══════════════════════════════════════════════════
        // GET CLASS INFORMATION
        // ═══════════════════════════════════════════════════
        Class<?> personClass = Person.class;
        System.out.println("Class name: " + personClass.getName());
        System.out.println("Simple name: " + personClass.getSimpleName());


        // ═══════════════════════════════════════════════════
        // GET CONSTRUCTORS
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== Constructors ===");
        Constructor<?>[] constructors = personClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }


        // ═══════════════════════════════════════════════════
        // INSTANTIATE USING REFLECTION
        // ═══════════════════════════════════════════════════
        Constructor<?> constructor = personClass.getDeclaredConstructor(String.class, int.class);
        Object person = constructor.newInstance("John", 30);
        System.out.println("\nCreated: " + person);


        // ═══════════════════════════════════════════════════
        // GET AND SET FIELDS
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== Fields ===");
        Field[] fields = personClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);  // Allow access to private fields
            System.out.println(field.getName() + " = " + field.get(person));

            if (field.getName().equals("age")) {
                field.set(person, 31);  // Change age
            }
        }
        System.out.println("After modification: " + person);


        // ═══════════════════════════════════════════════════
        // GET AND INVOKE METHODS
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== Methods ===");
        Method[] methods = personClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        // Invoke method
        Method greetMethod = personClass.getDeclaredMethod("greet");
        greetMethod.invoke(person);


        // ═══════════════════════════════════════════════════
        // CHECK ANNOTATIONS
        // ═══════════════════════════════════════════════════
        System.out.println("\n=== Annotations ===");
        Annotation[] annotations = personClass.getDeclaredAnnotations();
        System.out.println("Annotations: " + Arrays.toString(annotations));
    }
}
