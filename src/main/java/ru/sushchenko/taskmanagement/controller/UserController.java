package ru.sushchenko.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sushchenko.taskmanagement.dto.TaskResponseDto;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    // Добавить квери параметры на креатора и исполнителя
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<TaskResponseDto> getUserTasks(@PathVariable Long userId) {
        return null;
    }
}
