package ru.sushchenko.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.taskmanagement.dto.TaskResponseDto;
import ru.sushchenko.taskmanagement.service.TaskService;
import ru.sushchenko.taskmanagement.utils.exceptions.ControllerErrorResponse;
import ru.sushchenko.taskmanagement.utils.mapper.TaskMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Manipulating users")
public class UserController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    @Operation(
            summary = "Get user tasks",
            description = "Get user tasks by full filter and pagination"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getUserTasks(@PathVariable Long userId,
                                              @RequestParam(name = "creator", required = false) Boolean creator,
                                              @RequestParam(name = "executor", required = false) Boolean executor,
                                              @RequestParam(name = "status", required = false) Long statusId,
                                              @RequestParam(name = "priority", required = false) Long priorityId,
                                              @RequestParam(name = "offset", required = false) Integer offset,
                                              @RequestParam(name = "limit", required = false) Integer limit ) {
        return ResponseEntity.ok(
                taskService.getUserTasksWithFilter(userId, statusId,priorityId,creator,executor,offset,limit)
                .stream()
                .map(taskMapper::toDto)
                .toList()
        );
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
