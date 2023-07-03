package de.hadi.taskmanagement.exception;

public class TaskNotFoundExceptionRest extends RuntimeException {
    public TaskNotFoundExceptionRest(String message) {
        super(message);
    }
}