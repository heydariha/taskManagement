package de.fairsource.taskmanagement.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "task")
public class Task {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String name;

    private boolean done;

    private Instant created = Instant.now();

    @Enumerated(EnumType.STRING)
    private Priority priority;

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", done=" + done +
                ", created=" + created +
                ", priority=" + priority +
                '}';
    }
}
