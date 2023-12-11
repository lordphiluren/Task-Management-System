package ru.sushchenko.taskmanagment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sushchenko.taskmanagment.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

}
