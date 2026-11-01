package com.jfsd.mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Keeps the registered students in memory (lost when the application is redeployed).
 */
public class StudentStore {

    private final List<Student> students = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public Student add(String name, String email, String course) {
        Student s = new Student(nextId.getAndIncrement(), name, email, course);
        students.add(s);
        return s;
    }

    public boolean emailExists(String email) {
        return students.stream().anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
    }

    public void remove(int id) {
        students.removeIf(s -> s.getId() == id);
    }

    public List<Student> all() {
        return new ArrayList<>(students);
    }
}
