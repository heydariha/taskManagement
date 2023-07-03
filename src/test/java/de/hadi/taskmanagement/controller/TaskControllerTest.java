package de.hadi.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hadi.taskmanagement.domain.Priority;
import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.dto.TaskForm;
import de.hadi.taskmanagement.services.TaskService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WithMockUser(username = "user", roles = "USER")
public class TaskControllerTest {

    @MockBean
    private TaskService taskService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Main Dashboard")));
    }

    @Test
    public void testGetSingleTask() throws Exception {
        // GIVEN
        UUID taskId = mockTaskService();

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{taskId}", taskId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("selected=\"selected\">NORMAL</option>")));
    }

    @Test
    public void testCreateNewTask() throws Exception {
        // GIVEN
        TaskForm taskForm = new TaskForm();
        taskForm.setName("My task");
        taskForm.setDone(false);
        taskForm.setPriority(Priority.LOW);

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .with(csrf())
                        .content(asJsonString(taskForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("class=\"container mt-5\"")));
    }


    @Test
    public void testUpdateExistingTask() throws Exception {
        // GIVEN
        UUID taskId = mockTaskService();

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tasks/{taskId}", taskId.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "new task name")
                        .param("done", String.valueOf(true))
                        .param("priority", Priority.LOW.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    public void testDeleteExistingTask() throws Exception {
        // GIVEN
        UUID taskId = UUID.randomUUID();
        mockTaskService();

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/{taskId}", taskId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    private static String asJsonString(TaskForm taskForm) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(taskForm);
    }

    @NotNull
    private UUID mockTaskService() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task.Builder().
                withId(taskId).
                withDone(true).
                withPriority(Priority.NORMAL)
                .build();
        when(taskService.getTaskById(any())).thenReturn(Optional.of(task));

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task.Builder().build());
        tasks.add(task);
        when(taskService.getAllTasks()).thenReturn(tasks);

        return taskId;
    }
}
