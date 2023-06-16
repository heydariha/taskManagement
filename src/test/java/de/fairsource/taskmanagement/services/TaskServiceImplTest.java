package de.fairsource.taskmanagement.services;

import de.fairsource.taskmanagement.domain.Priority;
import de.fairsource.taskmanagement.domain.Task;
import de.fairsource.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
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
    void testDeleteTask() {
        // GIVEN
        UUID taskId = UUID.randomUUID();

        //WHEN
        taskService.deleteTask(taskId);

        //THEN
        verify(taskRepository, times(1)).deleteById(taskId);
    }

}
