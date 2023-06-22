package de.hadi.taskmanagement.integrationTest;

import de.hadi.taskmanagement.domain.Priority;
import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "user", password = "password", roles = "USER")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static TaskRepository taskRepository;
    private static Task task1;
    private static Task task2;

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        TaskControllerIntegrationTest.taskRepository = taskRepository;
    }

    @BeforeAll
    public void setUp() {
        Task tempTask1 = new Task.Builder()
                .withName("Task 1")
                .withDone(false)
                .withPriority(Priority.URGENT)
                .build();
        task1 = taskRepository.save(tempTask1);

        Task tempTask2 = new Task.Builder()
                .withName("Task 2")
                .withDone(true)
                .withPriority(Priority.NORMAL)
                .build();
        task2 = taskRepository.save(tempTask2);
    }

    @Test
    public void testGetSingleTask() throws Exception {
        mockMvc.perform(get("/tasks/{taskId}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("tasksHomePage"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("taskForm"));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasksHomePage"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("taskForm"));
    }

    @Test
    public void testCreateNewTask() throws Exception {
        mockMvc.perform(post("/tasks")
                        .with(csrf())
                        .param("name", "My Task")
                        .param("done", "true")
                        .param("priority", Priority.URGENT.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    public void testUpdateExistingTask() throws Exception {
        mockMvc.perform(put("/tasks/{taskId}", task1.getId()).with(csrf())
                        .param("name", "Updated Task")
                        .param("done", "true")
                        .param("priority", Priority.LOW.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
        Optional<Task> task = taskRepository.findById(task1.getId());
        assertEquals("Updated Task", task.get().getName());
    }

    @Test
    public void testDeleteExistingTask() throws Exception {
        mockMvc.perform(delete("/tasks/{taskId}", task2.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
        Optional<Task> task = taskRepository.findById(task2.getId());
        assertFalse(task.isPresent());
    }
}
