package de.hadi.taskmanagement.services;

import de.hadi.taskmanagement.domain.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Optional<Task> getTaskById(UUID taskId);

    List<Task> getAllTasks();

    Task createTask(Task task);

    Task updateTask(Task task);

    boolean deleteTask(UUID taskId);
}
