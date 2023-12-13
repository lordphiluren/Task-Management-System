package ru.sushchenko.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import ru.sushchenko.taskmanagement.utils.exceptions.ControllerErrorResponse;
import ru.sushchenko.taskmanagement.utils.exceptions.NotEnoughPrivilegesException;
import ru.sushchenko.taskmanagement.utils.mapper.CommentMapper;
import ru.sushchenko.taskmanagement.utils.mapper.TaskMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Manipulating tasks")
public class TaskController {
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    @Operation(
            summary = "Get all tasks",
            description = "Get all tasks by full filter and pagination"
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("")
    public ResponseEntity<List<TaskResponseDto>> getTasks(
                                                    @RequestParam(name = "status", required = false) Long statusId,
                                                    @RequestParam(name = "priority", required = false) Long priorityId,
                                                    @RequestParam(name = "offset", required = false) Integer offset,
                                                    @RequestParam(name = "limit", required = false) Integer limit) {

        return ResponseEntity.ok(taskService.getAllTasks(statusId, priorityId, offset, limit)
                .stream()
                .map(taskMapper::toDto)
                .toList());
    }
    @Operation(
            summary = "Add task",
            description = "Add task to system and set creator of currently authenticated user"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("")
    public ResponseEntity<?> createTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @Valid @RequestBody TaskRequestDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setCreator(User.builder().id(userPrincipal.getUserId()).build());
        taskService.addTask(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Operation(
            summary = "Get task by id",
            description = ""
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.getTaskById(taskId)));
    }
    @Operation(
            summary = "Edit task by id",
            description = "Edit task by id if currently authenticated user is creator or executor of the task"
    )
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId,
                                      @Valid @RequestBody TaskRequestDto taskDto,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if(taskService.checkUserIsRelatedToTask(taskId, userPrincipal.getUserId())) {
            Task task = taskMapper.toEntity(taskDto);
            task.setId(taskId);
            taskService.updateTask(task);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        throw new NotEnoughPrivilegesException("User with id: " + userPrincipal.getUserId() +
                "has no permission to manipulate task with id: " + taskId);
    }
    @Operation(
            summary = "Delete task by id",
            description = ""
    )
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if(taskService.checkUserIsRelatedToTask(taskId, userPrincipal.getUserId())) {
            taskService.deleteTaskById(taskId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            throw new NotEnoughPrivilegesException("User with id: " + userPrincipal.getUserId() +
                    " has no permission to manipulate task with id: " + taskId);
        }
    }
    @Operation(
            summary = "Add comment to task by id",
            description = ""
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long taskId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @Valid @RequestBody CommentRequestDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        // Call to database in order to check existence of task
        comment.setTask(taskService.getTaskById(taskId));
        comment.setCreator(User.builder().id(userPrincipal.getUserId()).build());
        commentService.addComment(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @ExceptionHandler
    private ResponseEntity<ControllerErrorResponse> handleException(RuntimeException e) {
        ControllerErrorResponse errorResponse = new ControllerErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        HttpStatus httpStatus = responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
    // validation exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
