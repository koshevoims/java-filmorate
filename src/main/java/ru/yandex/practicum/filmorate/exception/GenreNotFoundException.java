package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends Exception {
    public GenreNotFoundException(String message) {
        super(message);
    }

    public static class FilmNotFoundException extends Exception {
        public FilmNotFoundException(String message) {
            super(message);
        }
    }
}
