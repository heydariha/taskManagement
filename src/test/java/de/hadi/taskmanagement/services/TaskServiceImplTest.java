package de.hadi.taskmanagement.services;

import de.hadi.taskmanagement.domain.Priority;
import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.exception.TaskNotFoundExceptionRest;
import de.hadi.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testGetTaskById() {
        // GIVEN
        UUID taskId = UUID.randomUUID();
        Task newTask = new Task.Builder().withId(taskId).build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(newTask));

        //WHEN
        Optional<Task> result = taskService.getTaskById(taskId);

        //THEN
        assertEquals(Optional.of(newTask), result);
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testGetAllTasks() {
        // GIVEN
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task.Builder().withId(UUID.randomUUID()).build());
        tasks.add(new Task.Builder().withId(UUID.randomUUID()).build());
        when(taskRepository.findAll()).thenReturn(tasks);

        //WHEN
        List<Task> result = taskService.getAllTasks();

        //THEN
        assertEquals(tasks, result);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreateNewTask() {
        // GIVEN
        Task task = new Task.Builder()
                .withId(UUID.randomUUID())
                .withName("Hadi")
                .withDone(true)
                .withCreated(Instant.now())
                .withPriority(Priority.NORMAL)
                .build();
        when(taskRepository.save(task)).thenReturn(task);

        //WHEN
        Task result = taskService.createTask(task);

        //THEN
        assertEquals(task, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateExistingTask() {
        // GIVEN
        Task task = new Task.Builder()
                .withId(UUID.randomUUID())
                .build();
        when(taskRepository.save(task)).thenReturn(task);

        //WHEN
        Task result = taskService.updateTask(task);

        //THEN
        assertEquals(task, result);
        assertEquals(task.getId(), result.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteValidTaskShouldReturnTrue() {
        // GIVEN
        UUID taskId = UUID.randomUUID();
        Task task = new Task.Builder()
                .withId(UUID.randomUUID())
                .build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        //WHEN
        boolean result = taskService.deleteTask(taskId);

        // Assert the exception message
        assertTrue(result);
    }

    @Test
    void testDeleteInvalidTaskShouldTrowException() {
        // GIVEN
        UUID taskId = UUID.randomUUID();

        //WHEN
        // Verify the exception
        TaskNotFoundExceptionRest exception = assertThrows(TaskNotFoundExceptionRest.class, () -> {
            taskService.deleteTask(taskId);
        });

        // Assert the exception message
        assertTrue(exception.getMessage().contains("Task with ID "+ taskId +" not found."));
    }

}
