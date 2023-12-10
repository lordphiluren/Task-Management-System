package ru.sushchenko.taskmanagment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.taskmanagment.dto.TaskRequestDto;
import ru.sushchenko.taskmanagment.dto.TaskResponseDto;
import ru.sushchenko.taskmanagment.entity.Task;
import ru.sushchenko.taskmanagment.security.UserPrincipal;
import ru.sushchenko.taskmanagment.service.TaskService;
import ru.sushchenko.taskmanagment.service.UserService;
import ru.sushchenko.taskmanagment.utils.mapper.TaskMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final UserService userService;
    @GetMapping("")
    public List<TaskResponseDto> getTasks() {
        return taskService.getAllTasks()
                .stream()
                .map(taskMapper::git toDto)
                .toList();
    }

    @PostMapping("")
    public ResponseEntity<?> createTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestBody TaskRequestDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setCreator(userService.getUserById(userPrincipal.getUserId()));
        taskService.addTask(task);
        return ResponseEntity.ok("Task was successfully created");
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long taskId) {
        return null;
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId) {
        return null;
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        return null;
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> editTaskStatus(@PathVariable Long taskId) {
        return null;
    }

    @PatchMapping("/{taskId}/executor")
    public ResponseEntity<?> editTaskExecutor(@PathVariable Long taskId) {
        return null;
    }

    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<?> editTaskPriority(@PathVariable Long taskId) {
        return null;
    }

}
