package ru.sushchenko.taskmanagment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<TaskResponseDto>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks()
                .stream()
                .map(taskMapper::toDto)
                .toList());
    }

    @PostMapping("")
    public ResponseEntity<?> createTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestBody TaskRequestDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setCreator(userService.getUserById(userPrincipal.getUserId()));
        taskService.addTask(task);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.getTaskById(taskId)));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId, @RequestBody TaskRequestDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setId(taskId);
        taskService.updateTask(task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}