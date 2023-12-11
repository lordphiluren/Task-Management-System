package ru.sushchenko.taskmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.taskmanagement.dto.AuthRequest;
import ru.sushchenko.taskmanagement.dto.AuthResponse;
import ru.sushchenko.taskmanagement.entity.User;
import ru.sushchenko.taskmanagement.service.AuthService;
import ru.sushchenko.taskmanagement.utils.exceptions.ControllerErrorResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.attemptLogin(authRequest.getEmail(), authRequest.getPassword());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AuthRequest authRequest) {
        authService.signUp(modelMapper.map(authRequest, User.class));
        return ResponseEntity.ok("Successful registration");
    }

    @ExceptionHandler
    private ResponseEntity<ControllerErrorResponse> handleException(RuntimeException e) {
        ControllerErrorResponse errorResponse = new ControllerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
