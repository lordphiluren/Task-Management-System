package ru.sushchenko.taskmanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sushchenko.taskmanagement.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

}
