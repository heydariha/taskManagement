package de.hadi.taskmanagement.dto;

import de.hadi.taskmanagement.domain.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class TaskDto {

        private UUID uuid;
        @NotBlank(message = "Name is required")
        private String name;

        @NotNull
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

        public UUID getUuid() {
                return this.uuid;
        }
        public void setUuid(UUID uuid) {
                this.uuid = uuid;
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
