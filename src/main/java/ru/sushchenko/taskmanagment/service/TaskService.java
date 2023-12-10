package ru.sushchenko.taskmanagment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagment.entity.Task;
import ru.sushchenko.taskmanagment.repo.TaskRepo;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;
    @Transactional
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }
    @Transactional
    public void addTask(Task task) {
        task.setCreatedAt(new Date());
        taskRepo.save(task);
    }
}
