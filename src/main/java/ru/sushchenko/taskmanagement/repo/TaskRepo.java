package ru.sushchenko.taskmanagement.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sushchenko.taskmanagement.entity.Task;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task AS t WHERE (t.creator = ?1 OR t.executor = ?1) AND t.priority = ?2 AND t.status = ?3")
    List<Task> findTasksByUserIdWithFilter(Long userId, Long priorityId, Long statusId);
    @Query("SELECT t FROM Task AS t WHERE (t.creator = ?1 OR t.executor = ?1) AND t.priority = ?2 AND t.status = ?3")
    Page<Task> findTasksByUserIdWithFilter(Long userId, Long priorityId, Long statusId, Pageable pageable);

}
