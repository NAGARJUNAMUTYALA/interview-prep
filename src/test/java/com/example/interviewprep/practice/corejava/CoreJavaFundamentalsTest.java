package com.example.interviewprep.practice.corejava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Core Java Fundamentals: Memory, Object Contracts & Immutability")
class CoreJavaFundamentalsTest {

    // ─── 1. Pass-by-Value Demonstration ─────────────────────────────────────────

    static class Person {
        String name;
        Person(String name) { this.name = name; }
    }

    private void modifyPrimitive(int x) {
        x = 99; // Modifies local stack copy only
    }

    private void reassignReference(Person p) {
        p = new Person("Reassigned"); // Modifies local reference variable only
    }

    private void mutateObject(Person p) {
        p.name = "Mutated"; // Modifies the object in the Heap
    }

    @Test
    @DisplayName("Java is strictly pass-by-value: primitives and object references")
    void testPassByValueMechanics() {
        int originalInt = 10;
        modifyPrimitive(originalInt);
        assertEquals(10, originalInt, "Primitive value on stack must remain unchanged");

        Person person = new Person("Original");
        reassignReference(person);
        assertEquals("Original", person.name, "Reference reassignment in method does not change caller's reference");

        mutateObject(person);
        assertEquals("Mutated", person.name, "Object mutation in heap through copied reference is visible to caller");
    }

    // ─── 2. String Pool & Immutability ──────────────────────────────────────────

    @Test
    @DisplayName("String Constant Pool and intern() mechanics")
    void testStringConstantPool() {
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");

        assertSame(s1, s2, "String literals point to the identical object in the String Constant Pool");
        assertNotSame(s1, s3, "new String() creates a distinct object on the Heap");
        assertEquals(s1, s3, "equals() checks logical content equality");

        String s4 = s3.intern();
        assertSame(s1, s4, "intern() returns reference from the String Constant Pool");
    }

    // ─── 3. equals() & hashCode() Contract ──────────────────────────────────────

    static final class EmployeeKey {
        private final int id;
        private final String department;

        EmployeeKey(int id, String department) {
            this.id = id;
            this.department = department;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true; // Reflexive
            if (!(o instanceof EmployeeKey that)) return false; // Non-nullity & type
            return id == that.id && Objects.equals(department, that.department); // Symmetric & Transitive
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, department); // Consistent with equals
        }
    }

    @Test
    @DisplayName("equals() and hashCode() contract compliance")
    void testEqualsAndHashCodeContract() {
        EmployeeKey key1 = new EmployeeKey(101, "ENG");
        EmployeeKey key2 = new EmployeeKey(101, "ENG");
        EmployeeKey key3 = new EmployeeKey(101, "ENG");

        // Reflexive
        assertTrue(key1.equals(key1));
        // Symmetric
        assertTrue(key1.equals(key2) && key2.equals(key1));
        // Transitive
        assertTrue(key1.equals(key2) && key2.equals(key3) && key1.equals(key3));
        // Non-nullity
        assertFalse(key1.equals(null));
        // hashCode consistency
        assertEquals(key1.hashCode(), key2.hashCode(), "Equal objects must have identical hash codes");
    }

    // ─── 4. Generics PECS (Producer Extends, Consumer Super) ────────────────────

    static class Animal { String speak() { return "sound"; } }
    static class Dog extends Animal { @Override String speak() { return "bark"; } }

    @Test
    @DisplayName("Generics PECS Principle demonstration")
    void testGenericsPecs() {
        // Producer Extends: read-only from producer
        List<Dog> dogs = List.of(new Dog(), new Dog());
        List<? extends Animal> animalProducer = dogs;
        Animal first = animalProducer.get(0);
        assertEquals("bark", first.speak());

        // Consumer Super: write into consumer
        List<Animal> animalList = new ArrayList<>();
        List<? super Dog> dogConsumer = animalList;
        dogConsumer.add(new Dog());
        assertEquals(1, animalList.size());
    }
}
