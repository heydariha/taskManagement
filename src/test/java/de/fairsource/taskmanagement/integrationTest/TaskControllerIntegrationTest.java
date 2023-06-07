package de.fairsource.taskmanagement.integrationTest;

import de.fairsource.taskmanagement.domain.Priority;
import de.fairsource.taskmanagement.domain.Task;
import de.fairsource.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        Task tempTask1 = new Task();
        tempTask1.setName("Task 1");
        tempTask1.setDone(false);
        tempTask1.setPriority(Priority.URGENT);
        task1 = taskRepository.save(tempTask1);

        Task tempTask2 = new Task();
        tempTask2.setName("Task 2");
        tempTask2.setDone(true);
        tempTask2.setPriority(Priority.NORMAL);
        task2 = taskRepository.save(tempTask2);
    }

    @Test
    public void testGetSingleTask() throws Exception {
        mockMvc.perform(get("/tasks/{taskId}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("taskForm"));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("taskForm"));
    }

    @Test
    public void testCreateNewTask() throws Exception {
        mockMvc.perform(post("/tasks/")
                        .param("name", "My Task")
                        .param("done", "true")
                        .param("priority", Priority.URGENT.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/"));
    }

    @Test
    public void testUpdateExistingTask() throws Exception {
        mockMvc.perform(put("/tasks/{taskId}", task1.getId())
                        .param("name", "Updated Task")
                        .param("done", "true")
                        .param("priority", Priority.LOW.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/"));
        Optional<Task> task = taskRepository.findById(task1.getId());
        assertEquals("Updated Task", task.get().getName());
    }

    @Test
    public void testDeleteExistingTask() throws Exception {
        mockMvc.perform(delete("/tasks/{taskId}", task2.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/"));
        Optional<Task> task = taskRepository.findById(task2.getId());
        assertFalse(task.isPresent());
    }
}
