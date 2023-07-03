package de.hadi.taskmanagement.controller;

import de.hadi.taskmanagement.domain.Priority;
import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.dto.TaskDto;
import de.hadi.taskmanagement.helper.TaskTranslator;
import de.hadi.taskmanagement.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskRestControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private RestTemplate restTemplate;

    private TaskService taskService;
    private TaskRestController controller;

    public TaskRestControllerTest() {
        taskService = Mockito.mock(TaskService.class);
        controller = new TaskRestController(taskService);
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/tasks";
    }

    @Test
    void getAllTasksReturnsListOfTasks() {
        //GIVEN
        Task task1 = new Task.Builder().
                withId(UUID.randomUUID()).
                withName("Hadi Heydari").
                withDone(true).
                withPriority(Priority.NORMAL).
                build();
        Task task2 = new Task.Builder().
                withId(UUID.randomUUID()).
                withName("Max Munster").
                withDone(false).
                withPriority(Priority.URGENT).
                build();

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        // WHEN
        ResponseEntity<List<TaskDto>> response = controller.getAllTasks();

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getExistingTasksReturnsTaskDto() {
        //GIVEN
        UUID taskId = UUID.randomUUID();
        Task task = new Task.Builder().
                withId(taskId).
                withName("Max Munster").
                withDone(true).
                withPriority(Priority.NORMAL).
                build();
        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        // WHEN
        ResponseEntity<TaskDto> response = controller.getTaskById(taskId);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getNonExistingTaskByIdReturnsNotFound() {
        //GIVEN
        UUID taskId = UUID.randomUUID();
        String url = baseUrl + "/" + taskId.toString();

        //THEN
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<TaskDto> response = restTemplate.getForEntity(url, TaskDto.class);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createValidTaskShouldReturnDto() {
        //GIVEN
        String url = baseUrl;
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Hadi");
        taskDto.setPriority(Priority.URGENT);
        taskDto.setDone(true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskDto> request = new HttpEntity<>(taskDto, headers);

        //WHEN - Send POST request
        ResponseEntity<TaskDto> response = restTemplate.postForEntity(url, request, TaskDto.class);

        //THEN - Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateExistingTaskWithValidDtoShouldReturnDto() {
        //GIVEN
        UUID taskId = UUID.randomUUID();
        TaskDto taskDto = new TaskDto();
        taskDto.setUuid(taskId);
        taskDto.setName("Hadi");
        taskDto.setDone(true);
        taskDto.setPriority(Priority.URGENT);

        Task task = new Task.Builder().
                withId(taskId).
                withName("Max Munster").
                withDone(true).
                withPriority(Priority.NORMAL).
                build();

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));
        when(taskService.updateTask(task)).thenReturn(new TaskTranslator().translateToTask(taskDto));

        //WHEN - Send PUT request
        ResponseEntity<TaskDto> response = controller.updateTask(taskId, taskDto);

        //THEN - Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateTaskNonExistingTaskWithValidDtoReturnsNotFound() {
        //GIVEN
        UUID taskId = UUID.randomUUID();
        TaskDto taskDto = new TaskDto();
        taskDto.setUuid(taskId);
        taskDto.setName("Hadi");
        taskDto.setDone(true);
        taskDto.setPriority(Priority.URGENT);

        //WHEN - Send PUT request
        ResponseEntity<TaskDto> response = controller.updateTask(taskId, taskDto);

        //THEN - Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteExistingTaskShouldReturnsNoContent() {
        //GIVEN
        UUID taskId = UUID.randomUUID();
        when(taskService.deleteTask(taskId)).thenReturn(true);

        //WHEN
        ResponseEntity<Void> response = controller.deleteTask(taskId);

        //THEN
        verify(taskService, times(1)).deleteTask(taskId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteNonExistingTaskShouldReturnsNotFound() {
        UUID taskId = UUID.randomUUID();
        String url = baseUrl + "/" + taskId;

        // Verify the exception
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        });

        // Assert the exception message
        assertTrue(exception.getMessage().contains("Task with ID "+ taskId +" not found."));
    }
}