package de.fairsource.taskmanagement.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public String handleTaskNotFoundException(TaskNotFoundException ex, Model model) {
        model.addAttribute("exception", ex);
        return "error";
    }

}
