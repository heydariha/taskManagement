package de.hadi.taskmanagement.dto;

import de.hadi.taskmanagement.domain.Priority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

public class TaskForm {

        @NotBlank(message = "Name is required")
        private String name;

        private boolean done;

        @Enumerated(EnumType.STRING)
        private Priority priority;

        public String getName() {
                return name;
        }

        public boolean isDone() {
                return done;
        }

        public Priority getPriority() {
                return priority;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setDone(boolean done) {
                this.done = done;
        }

        public void setPriority(Priority priority) {
                this.priority = priority;
        }
}
