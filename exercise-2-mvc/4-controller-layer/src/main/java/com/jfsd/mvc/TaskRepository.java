package com.jfsd.mvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Small in-memory model so the controller has something to work with.
 */
public class TaskRepository {

    private final Map<Integer, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public TaskRepository() {
        save(new Task(0, "Read the servlet life cycle notes", true));
        save(new Task(0, "Finish the MVC lab exercise", false));
        save(new Task(0, "Revise Hibernate mappings", false));
    }

    public List<Task> findAll() {
        List<Task> list = new ArrayList<>(tasks.values());
        list.sort(Comparator.comparingInt(Task::getId));
        return list;
    }

    public Task findById(int id) {
        return tasks.get(id);
    }

    public void save(Task task) {
        if (task.getId() == 0) {
            task.setId(nextId.getAndIncrement());
        }
        tasks.put(task.getId(), task);
    }

    public void delete(int id) {
        tasks.remove(id);
    }
}
