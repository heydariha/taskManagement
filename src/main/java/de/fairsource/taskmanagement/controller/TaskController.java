package de.fairsource.taskmanagement.controller;

import de.fairsource.taskmanagement.domain.Task;
import de.fairsource.taskmanagement.exception.TaskNotFoundException;
import de.fairsource.taskmanagement.forms.TaskForm;
import de.fairsource.taskmanagement.services.TaskService;
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
        return "home";
    }

    @GetMapping("/")
    public String landingPage() {
        return "redirect:/tasks/";
    }
    @GetMapping("tasks/")
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("title", "Task management");
        model.addAttribute("taskForm", new TaskForm());
        return "home";
    }

    @PostMapping("tasks/")
    public String createNewTask(@Valid @ModelAttribute("taskForm") TaskForm taskForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName(taskForm.getName());
        task.setDone(taskForm.isDone());
        task.setPriority(taskForm.getPriority());
        taskService.createTask(task);
        return "redirect:/tasks/";
    }

    @PutMapping("tasks/{taskId}")
    public String updateExistingTask(@PathVariable String taskId, @Valid @ModelAttribute TaskForm taskForm) {
        Task existTask = getTask(UUID.fromString(taskId));
        existTask.setName(taskForm.getName());
        existTask.setDone(taskForm.isDone());
        existTask.setPriority(taskForm.getPriority());

        taskService.updateTask(existTask);
        return "redirect:/tasks/";
    }


    @DeleteMapping("tasks/{taskId}")
    public String deleteExistingTask(@PathVariable UUID taskId) {
        getTask(taskId);
        taskService.deleteTask(taskId);
        return "redirect:/tasks/";
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
