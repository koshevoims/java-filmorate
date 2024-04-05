package ru.yandex.practicum.filmorate.exception;

public class IncorrectGenreException extends RuntimeException {
    public IncorrectGenreException(String message) {
        super(message);
    }
}
