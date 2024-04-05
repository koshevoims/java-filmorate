package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnknownFilmException(final FilmNotFoundException e) {
        log.debug("404 Not found film - Страница не найдена {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUnknownUserException(final UserNotFoundException e) {
        log.debug("404 Not found user - Страница не найдена {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleMpaNotFoundException(final MpaNotFoundException e) {
        log.debug("404 Not found mpa - Страница не найдена {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleGenreNotFoundException(final GenreNotFoundException e) {
        log.debug("404 Not found genre - Страница не найдена {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIncorrectMpaException(final IncorrectMpaException e) {
        log.debug("400 Bad request mpa - Ошибка с полем: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIncorrectGenreException(final IncorrectGenreException e) {
        log.debug("400 Bad request genre - Ошибка с полем: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotValidException(final MethodArgumentNotValidException e) {
        log.debug("400 Bad request - Ошибка с полем: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleThrowableException(final Throwable e) {
        log.debug("500 Internal Server error: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                Map.of("Ошибка", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
