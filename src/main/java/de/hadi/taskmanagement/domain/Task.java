package de.hadi.taskmanagement.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String name;

    private boolean done;

    private Instant created;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private Task() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }

    public Priority getPriority() {
        return priority;
    }

    public Instant getCreated() {
        return created;
    }

    public UUID getId() {
        return id;
    }

    public static class Builder {
        private Task task;

        public Builder() {
            this.task = new Task();
        }

        public Builder withId(UUID id) {
            this.task.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.task.name = name;
            return this;
        }

        public Builder withDone(boolean done) {
            this.task.done = done;
            return this;
        }

        public Builder withCreated(Instant created) {
            this.task.created = created;
            return this;
        }

        public Builder withPriority(Priority priority) {
            this.task.priority = priority;
            return this;
        }

        public Task build() {
            if (task.created == null) {
                task.created = Instant.now();
            }
            return task;
        }
    }
}


//@Entity
//@Table(name = "task")
//public class Task {
//
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    private UUID id;
//
//    private String name;
//
//    private boolean done;
//
//    private Instant created = Instant.now();
//
//    @Enumerated(EnumType.STRING)
//    private Priority priority;
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setDone(boolean done) {
//        this.done = done;
//    }
//
//    public void setCreated(Instant created) {
//        this.created = created;
//    }
//
//    public void setPriority(Priority priority) {
//        this.priority = priority;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public boolean isDone() {
//        return done;
//    }
//
//    public Priority getPriority() {
//        return priority;
//    }
//
//    public Instant getCreated() {
//        return created;
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    @Override
//    public String toString() {
//        return "Task{" +
//                "name='" + name + '\'' +
//                ", done=" + done +
//                ", created=" + created +
//                ", priority=" + priority +
//                '}';
//    }
//}
