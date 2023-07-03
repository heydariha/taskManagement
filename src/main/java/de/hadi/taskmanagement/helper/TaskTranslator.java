package de.hadi.taskmanagement.helper;

import de.hadi.taskmanagement.domain.Task;
import de.hadi.taskmanagement.dto.TaskDto;

import java.util.UUID;

public class TaskTranslator {
    public TaskDto translateToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setUuid(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDone(task.isDone());
        taskDto.setPriority(task.getPriority());
        return taskDto;
    }

    public Task translateToTask(TaskDto taskDto) {

        Task task = new Task.Builder()
                .withId(taskDto.getUuid() != null ? taskDto.getUuid() : UUID.randomUUID())
                .withName(taskDto.getName())
                .withDone(taskDto.isDone())
                .withPriority(taskDto.getPriority()).build();
        return task;
    }

}
