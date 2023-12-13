package ru.sushchenko.taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.taskmanagement.entity.Priority;
import ru.sushchenko.taskmanagement.entity.Status;
import ru.sushchenko.taskmanagement.entity.Task;
import ru.sushchenko.taskmanagement.entity.User;
import ru.sushchenko.taskmanagement.repo.TaskRepo;
import ru.sushchenko.taskmanagement.utils.exceptions.TaskNotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;

    @Transactional
    // Return list of tasks with full filter and pagination
    public List<Task> getAllTasks(Long statusId, Long priorityId,
                                  Integer offset, Integer limit) {
        Task task = new Task();
        task.setStatus(Status.builder().id(statusId).build());
        task.setPriority(Priority.builder().id(priorityId).build());
        return getTasksFromMatcher(offset, limit, task);
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
    @Transactional
    // Get all tasks by user id with full filter
    public List<Task> getUserTasksWithFilter(Long userId, Long statusId, Long priorityId,
                                             Boolean creator, Boolean executor, Integer offset,
                                             Integer limit) {

        if (creator == null && executor == null) {
            return getAllTasksByUserId(userId, statusId, priorityId, offset, limit);
        }

        if (creator != null && creator) {
            return getAllTasksByCreatorId(userId, statusId, priorityId, offset, limit);
        }

        if (executor != null && executor) {
            return getAllTasksByExecutorId(userId, statusId, priorityId, offset, limit);
        }
        return Collections.emptyList();
    }
    @Transactional
    // Return all user tasks with filter and pagination
    public List<Task> getAllTasksByUserId(Long userId, Long statusId,
                                          Long priorityId, Integer offset, Integer limit) {
        if(offset != null && limit != null) {
            Pageable pageable = PageRequest.of(offset, limit);
            return taskRepo.findTasksByUserIdWithFilter(userId, statusId, priorityId, pageable).getContent();
        }
        return taskRepo.findTasksByUserIdWithFilter(userId, statusId, priorityId);
    }
    @Transactional
    // Get all created tasks with filter by status and priority
    public List<Task> getAllTasksByCreatorId(Long userId, Long statusId,
                                             Long priorityId, Integer offset, Integer limit) {
        Task task = new Task();
        task.setStatus(Status.builder().id(statusId).build());
        task.setPriority(Priority.builder().id(priorityId).build());
        task.setCreator(User.builder().id(userId).build());

        return getTasksFromMatcher(offset, limit, task);
    }
    @Transactional
    // Get all assigned tasks with filter by status and priority
    public List<Task> getAllTasksByExecutorId(Long userId, Long statusId,
                                              Long priorityId, Integer offset, Integer limit) {
        Task task = new Task();
        task.setStatus(Status.builder().id(statusId).build());
        task.setPriority(Priority.builder().id(priorityId).build());
        task.setExecutor(User.builder().id(userId).build());

        return getTasksFromMatcher(offset, limit, task);
    }
    // Return list of tasks from matcher with pagination if set
    private List<Task> getTasksFromMatcher(Integer offset, Integer limit, Task task) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues();
        Example<Task> example = Example.of(task, matcher);
        if(offset != null && limit != null) {
            Pageable pageable = PageRequest.of(offset, limit);
            return taskRepo.findAll(example, pageable).getContent();
        }
        return taskRepo.findAll(example);
    }
    @Transactional
    // Check if user is creator or executor of the task
    public boolean checkUserIsRelatedToTask(Long taskId, Long userId) {
        Task task = getTaskById(taskId);
        User executor = task.getExecutor();
        return Objects.equals(task.getCreator().getId(), userId)
                || (executor != null && Objects.equals(executor.getId(), userId));
    }
}
