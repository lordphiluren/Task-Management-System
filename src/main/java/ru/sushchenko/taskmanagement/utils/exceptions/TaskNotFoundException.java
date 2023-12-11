package ru.sushchenko.taskmanagement.utils.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String msg) {
        super(msg);
    }
}
