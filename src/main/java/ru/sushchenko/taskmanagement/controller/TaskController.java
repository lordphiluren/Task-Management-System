package ru.sushchenko.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.taskmanagement.dto.CommentRequestDto;
import ru.sushchenko.taskmanagement.dto.TaskRequestDto;
import ru.sushchenko.taskmanagement.dto.TaskResponseDto;
import ru.sushchenko.taskmanagement.entity.Comment;
import ru.sushchenko.taskmanagement.entity.Task;
import ru.sushchenko.taskmanagement.entity.User;
import ru.sushchenko.taskmanagement.security.UserPrincipal;
import ru.sushchenko.taskmanagement.service.CommentService;
import ru.sushchenko.taskmanagement.service.TaskService;
import ru.sushchenko.taskmanagement.service.UserService;
import ru.sushchenko.taskmanagement.utils.exceptions.ControllerErrorResponse;
import ru.sushchenko.taskmanagement.utils.mapper.CommentMapper;
import ru.sushchenko.taskmanagement.utils.mapper.TaskMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
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
        task.setCreator(User.builder().id(userPrincipal.getUserId()).build());
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
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long taskId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestBody CommentRequestDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        // Call to database in order to check existence of task
        comment.setTask(taskService.getTaskById(taskId));
        comment.setCreator(User.builder().id(userPrincipal.getUserId()).build());
        commentService.addComment(comment);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @ExceptionHandler
    private ResponseEntity<ControllerErrorResponse> handleException(RuntimeException e) {
        ControllerErrorResponse errorResponse = new ControllerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
