package ru.sushchenko.taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagement.entity.Task;
import ru.sushchenko.taskmanagement.entity.User;
import ru.sushchenko.taskmanagement.repo.TaskRepo;
import ru.sushchenko.taskmanagement.utils.exceptions.NotEnoughPrivilegesException;
import ru.sushchenko.taskmanagement.utils.exceptions.TaskNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    // Checks that user is creator or executor of the task
    @Transactional
    public boolean checkUserIsRelatedToTask(Long taskId, Long userId) {
        Task task = getTaskById(taskId);
        User executor = task.getExecutor();
        return Objects.equals(task.getCreator().getId(), userId) || (executor != null && Objects.equals(executor.getId(), userId));
    }
}
