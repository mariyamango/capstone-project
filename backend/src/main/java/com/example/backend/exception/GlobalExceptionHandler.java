package com.example.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Some error has occurred. Please contact the administrator.";
    public static final String ACCESS_ERROR_MESSAGE = "Access denied for this user.";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleUnexpectedExceptions(Exception e) {
        logExceptions(e);
        return new ErrorMessage(INTERNAL_SERVER_ERROR_MESSAGE);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleAccessDeniedException(AccessDeniedException e) {
        logExceptions(e);
        return new ErrorMessage(ACCESS_ERROR_MESSAGE);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleExpectedExceptions(IllegalArgumentException e) {
        logExceptions(e);
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NoSuchElementException e) {
        logExceptions(e);
        return new ErrorMessage(e.getMessage());
    }

    private void logExceptions(Exception e) {
        log.error("Exceptions caught in global exception handler: {}", e.getMessage(), e);
    }

}