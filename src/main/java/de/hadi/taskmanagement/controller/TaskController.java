package de.hadi.taskmanagement.controller;

import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.exception.TaskNotFoundException;
import de.hadi.taskmanagement.forms.TaskForm;
import de.hadi.taskmanagement.services.TaskService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("tasks/{taskId}")
    public String getSingleTask(@PathVariable String taskId, Model model) {
        Task existTask = getTask(UUID.fromString(taskId));
        model.addAttribute("task", existTask);
        model.addAttribute("title", "Task management");
        model.addAttribute("taskForm", existTask);
        return "tasksHomePage";
    }

    @GetMapping({"tasks", "tasks/"})
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "Main Dashboard");
        model.addAttribute("taskForm", new TaskForm());
        return "tasksHomePage";
    }

    @PostMapping("tasks")
    public String createNewTask(@Valid @ModelAttribute("taskForm") TaskForm taskForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasksHomePage";
        }
        Task task = new Task.Builder()
                .withId(UUID.randomUUID())
                .withName(taskForm.getName())
                .withDone(taskForm.isDone())
                .withPriority(taskForm.getPriority()).build();
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @PutMapping("tasks/{taskId}")
    public String updateExistingTask(@PathVariable String taskId, @Valid @ModelAttribute TaskForm taskForm) {
        Task existTask = getTask(UUID.fromString(taskId));
        existTask.setName(taskForm.getName());
        existTask.setDone(taskForm.isDone());
        existTask.setPriority(taskForm.getPriority());

        taskService.updateTask(existTask);
        return "redirect:/tasks";
    }


    @DeleteMapping("tasks/{taskId}")
    public String deleteExistingTask(@PathVariable UUID taskId) {
        getTask(taskId);
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }

    @NotNull
    private Task getTask(UUID taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if(!task.isPresent()){
            throw new TaskNotFoundException("Task with ID " + taskId + " not found.");
        }
        return task.get();
    }
}
