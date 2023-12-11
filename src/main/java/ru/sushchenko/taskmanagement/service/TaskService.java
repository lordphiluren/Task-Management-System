package ru.sushchenko.taskmanagment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagment.entity.Task;
import ru.sushchenko.taskmanagment.repo.TaskRepo;
import ru.sushchenko.taskmanagment.utils.exceptions.TaskNotFoundException;

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
    @Transactional
    public Task getTaskById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + "doesn't exist"));
    }
    @Transactional
    public void deleteTaskById(Long id) {
        taskRepo.deleteById(id);
    }
    @Transactional
    public void updateTask(Task task) {
        taskRepo.save(task);
    }
}
