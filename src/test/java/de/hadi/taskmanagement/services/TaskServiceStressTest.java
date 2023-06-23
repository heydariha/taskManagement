package de.hadi.taskmanagement.services;

import de.hadi.taskmanagement.domain.Priority;
import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Execution(ExecutionMode.CONCURRENT)
public class TaskServiceStressTest {
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskServiceStressTest() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    public void stressTestCreateUpdateDelete() throws InterruptedException {
        int numThreads = 100;
        int numTasks = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        when(taskRepository.save(Mockito.any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Submit tasks for concurrent execution
        for (int i = 0; i < numTasks; i++) {
            executorService.submit(() -> {
                Task task = new Task.Builder()
                        .withId(UUID.randomUUID())
                        .withName("Hadi")
                        .withDone(true)
                        .withCreated(Instant.now())
                        .withPriority(Priority.NORMAL)
                        .build();

                Task createdTask = taskService.createTask(task);
                assertEquals(task, createdTask);
                Task updatedTask = new Task.Builder()
                        .withId(UUID.randomUUID())
                        .withName("Updated Task Name")
                        .withDone(true)
                        .withCreated(Instant.now())
                        .withPriority(Priority.NORMAL)
                        .build();
                Task savedTask = taskService.updateTask(updatedTask);
                assertEquals(updatedTask, savedTask);

                taskService.deleteTask(savedTask.getId());
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
}
