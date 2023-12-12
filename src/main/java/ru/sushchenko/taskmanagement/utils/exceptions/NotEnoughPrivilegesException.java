package ru.sushchenko.taskmanagement.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotEnoughPrivilegesException extends RuntimeException {
    public NotEnoughPrivilegesException(String msg) {
        super(msg);
    }
}
