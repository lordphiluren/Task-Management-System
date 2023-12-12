package ru.sushchenko.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.taskmanagement.dto.TaskResponseDto;
import ru.sushchenko.taskmanagement.entity.Task;
import ru.sushchenko.taskmanagement.entity.User;
import ru.sushchenko.taskmanagement.service.TaskService;
import ru.sushchenko.taskmanagement.service.UserService;
import ru.sushchenko.taskmanagement.utils.exceptions.ControllerErrorResponse;
import ru.sushchenko.taskmanagement.utils.mapper.TaskMapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TaskMapper taskMapper;
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getUserTasks(@PathVariable Long userId,
                                              @RequestParam(name = "creator", required = false) Boolean creator,
                                              @RequestParam(name = "executor", required = false) Boolean executor) {
        User user = userService.getUserById(userId);
        List<Task> createdTasks = user.getCreatedTasks();
        List<Task> assignedTasks = user.getAssignedTasks();
        List<Task> allTasks = new ArrayList<>();
        // Check both params and add all tasks
        if (creator == null && executor == null) {
            allTasks.addAll(createdTasks);
            allTasks.addAll(assignedTasks);
        }
        // Check creator param and add only created tasks
        if (creator != null && creator) {
            allTasks.addAll(createdTasks);
        }
        // Check executor param and add only executed tasks
        if (executor != null && executor) {
            allTasks.addAll(assignedTasks);
        }

        return ResponseEntity.ok(allTasks.stream()
                .map(taskMapper::toDto)
                .toList());
    }
    @ExceptionHandler
    private ResponseEntity<ControllerErrorResponse> handleException(RuntimeException e) {
        ControllerErrorResponse errorResponse = new ControllerErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        HttpStatus httpStatus = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
