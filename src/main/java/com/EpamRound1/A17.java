package com.dipdeveloper;

class ClassLoaderExample {

    public static void main(String[] args) {

        // ═══════════════════════════════════════════════════
        // GET CLASS LOADER FOR DIFFERENT CLASSES
        // ═══════════════════════════════════════════════════

        // Application ClassLoader (our code)
        Class<?> myClass = ClassLoaderExample.class;
        ClassLoader appClassLoader = myClass.getClassLoader();
        System.out.println("ClassLoader for MyClass: " + appClassLoader);
        System.out.println("ClassLoader class: " + appClassLoader.getClass().getName());


        // Extension ClassLoader (parent)
        ClassLoader extClassLoader = appClassLoader.getParent();
        System.out.println("\nParent ClassLoader: " + extClassLoader);
        System.out.println("ClassLoader class: " + extClassLoader.getClass().getName());


        // Bootstrap ClassLoader (top - can be null)
        ClassLoader bootClassLoader = extClassLoader.getParent();
        System.out.println("\nGrandparent ClassLoader: " + bootClassLoader);


        // ═══════════════════════════════════════════════════
        // BOOTSTRAP CLASSES (loaded by null)
        // ═══════════════════════════════════════════════════
        Class<?> stringClass = String.class;
        System.out.println("\nClassLoader for String: " + stringClass.getClassLoader());

        Class<?> intClass = Integer.class;
        System.out.println("ClassLoader for Integer: " + intClass.getClassLoader());


        // ═══════════════════════════════════════════════════
        // CUSTOM CLASS LOADER (Advanced)
        // ═══════════════════════════════════════════════════
        ClassLoader currentThreadLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("\nCurrent Thread ClassLoader: " + currentThreadLoader);
    }
}

// Output:
// ClassLoader for MyClass: jdk.internal.loader.ClassLoaders$AppClassLoader
// ClassLoader class: jdk.internal.loader.ClassLoaders$AppClassLoader
//
// Parent ClassLoader: jdk.internal.loader.ClassLoaders$PlatformClassLoader
// ClassLoader class: jdk.internal.loader.ClassLoaders$PlatformClassLoader
//
// Grandparent ClassLoader: null (Bootstrap ClassLoader)
//
// ClassLoader for String: null (Loaded by Bootstrap ClassLoader)
// ClassLoader for Integer: null (Loaded by Bootstrap ClassLoader)
//
// Current Thread ClassLoader: jdk.internal.loader.ClassLoaders$AppClassLoader
