package de.hadi.taskmanagement.repository;

import de.hadi.taskmanagement.domain.Task;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public abstract class TaskRepositoryImpl implements TaskRepository {
    private final JpaRepository<Task, UUID> jpaRepository;

    @Autowired
    public TaskRepositoryImpl(JpaRepository<Task, UUID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public synchronized Optional<Task> findById(@NotNull UUID taskId) {
        return jpaRepository.findById(taskId);
    }

    @Override
    public synchronized void deleteById(UUID taskId) {
        jpaRepository.deleteById(taskId);
    }
}
