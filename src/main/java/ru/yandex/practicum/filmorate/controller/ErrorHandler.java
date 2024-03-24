package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class})
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFountHandler(final ObjectNotFoundException e) {
        log.debug("404 Not found - Страница не найдена {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectParameterHandler(final ValidationException e) {
        log.debug("400 Bad request - Ошибка с полем: {}", e.getMessage(), e);
        return new ErrorResponse(String.format("Ошибка с полем: {}.", e.getParameter()));
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable e) {
        log.debug("5** Server error - Произошла непредвиденная ошибка {}", e.getMessage(), e);
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
