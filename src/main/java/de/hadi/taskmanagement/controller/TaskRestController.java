package de.hadi.taskmanagement.controller;

import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.dto.TaskDto;
import de.hadi.taskmanagement.helper.TaskTranslator;
import de.hadi.taskmanagement.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;
    private final TaskTranslator taskTranslator;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
        this.taskTranslator = new TaskTranslator();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable UUID taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isPresent()) {
            TaskDto taskDto = taskTranslator.translateToDto(task.get());
            return ResponseEntity.ok(taskDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {

        List<Task> tasks = taskService.getAllTasks();
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskTranslator::translateToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDtos);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskDto) {
        Task task = taskTranslator.translateToTask(taskDto);
        Task createdTask = taskService.createTask(task);
        TaskDto createdTaskDto = taskTranslator.translateToDto(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable UUID taskId, @RequestBody @Valid TaskDto taskDto) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isPresent()) {
            Task currentTask = task.get();
            currentTask.setName(taskDto.getName());
            currentTask.setDone(taskDto.isDone());
            currentTask.setPriority(taskDto.getPriority());
            Task updatedTask = taskService.updateTask(currentTask);
            TaskDto updatedTaskDto = taskTranslator.translateToDto(updatedTask);
            return ResponseEntity.ok(updatedTaskDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
