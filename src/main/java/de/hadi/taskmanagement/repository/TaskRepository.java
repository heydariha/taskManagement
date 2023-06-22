package de.hadi.taskmanagement.repository;

import de.hadi.taskmanagement.domain.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findById(@NotNull UUID taskId);

    void deleteById(UUID taskId);
}
