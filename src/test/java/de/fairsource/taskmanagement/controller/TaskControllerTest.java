package de.fairsource.taskmanagement.controller;

import de.fairsource.taskmanagement.domain.Priority;
import de.fairsource.taskmanagement.domain.Task;
import de.fairsource.taskmanagement.forms.TaskForm;
import de.fairsource.taskmanagement.services.TaskService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllTasks() {
        //GIVEN
        mockTaskService();

        //WHEN
        ResponseEntity<String> response = restTemplate.getForEntity("/tasks/", String.class);

        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("<div class=\"card\" "));
    }

    @Test
    public void testGetSingleTask() {
        //GIVEN
        UUID taskId = mockTaskService();

        //WHEN
        ResponseEntity<String> response = restTemplate.getForEntity("/tasks/{taskId}", String.class, taskId);

        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("selected=\"selected\">NORMAL</option>"));
    }

    @Test
    public void testCreateNewTask() {
        //GIVEN
        TaskForm taskForm = new TaskForm();
        taskForm.setName("My task");
        taskForm.setDone(false);
        taskForm.setPriority(Priority.LOW);
        HttpEntity<MultiValueMap<String, String>> requestEntity = generateHttpRequest(taskForm);

        //WHEN
        ResponseEntity<String> response = restTemplate.postForEntity("/tasks/", requestEntity, String.class);

        //THEN
        //After Insert, should redirect to task list page
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateNewTaskWithInvalidTaskName() {
        //GIVEN
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        TaskForm taskForm = new TaskForm();
        TaskController taskController = new TaskController(taskService);

        //WHEN
        String viewName = taskController.createNewTask(taskForm, bindingResult);

        //THEN
        assertEquals("home", viewName);
    }

    @Test
    public void testUpdateExistingTask() {
        //GIVEN
        UUID taskId = mockTaskService();
        TaskForm taskForm = new TaskForm();
        taskForm.setName("new task name");
        taskForm.setDone(true);
        taskForm.setPriority(Priority.LOW);
        HttpEntity<MultiValueMap<String, String>> requestEntity = generateHttpRequest(taskForm);

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/tasks/{taskId}", HttpMethod.PUT, requestEntity, String.class, taskId);

        //THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteExistingTask() {
        //GIVEN
        UUID taskId = UUID.randomUUID();
        mockTaskService();

        //WHEN
        ResponseEntity<Void> response = restTemplate.exchange("/tasks/{taskId}", HttpMethod.DELETE, null, Void.class, taskId);

        //THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }

    @NotNull
    private UUID mockTaskService() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task();
        task.setId(taskId);
        task.setDone(true);
        task.setPriority(Priority.NORMAL);
        when(taskService.getTaskById(any())).thenReturn(Optional.of(task));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(task);
        when(taskService.getAllTasks()).thenReturn(tasks);

        return taskId;
    }

    @NotNull
    private HttpEntity<MultiValueMap<String, String>> generateHttpRequest(TaskForm taskForm) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", taskForm.getName());
        requestParams.add("done", String.valueOf(taskForm.isDone()));
        requestParams.add("priority", taskForm.getPriority().toString());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);
        return requestEntity;
    }
}