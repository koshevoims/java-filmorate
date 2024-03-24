package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
class FilmControllerTest {
    private Validator validator;
    private List<String> message;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        message = new ArrayList<>();
    }

    @Test
    void testFilmCanNotEmptyName() {
        Film film = new Film(0, "", "description", LocalDate.now().minusDays(1), 10, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Name cannot be empty");
    }

    @Test
    void testFilmCanNotLongDescription() {
        Film film = new Film(0, "Terminator", "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription",
                LocalDate.now().minusDays(1), 10, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Description must be less than 200 characters");
    }

    @Test
    void testFilmCanNotReleaseDate() {
        Film film = new Film(0, "Terminator", "description", null, 10, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "releaseDate cannot be null");
    }

    @Test
    void testFilmValidReleaseDate() {
        Film film = new Film(0, "Terminator", "description", LocalDate.now().plusDays(1), 10, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "releaseDate cannot be in future");
    }

    @Test
    void testFilmCanNotNegativeDuration() {
        Film film = new Film(0, "Terminator", "description", LocalDate.now().minusDays(1), -10, null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Duration cannot be negative");
    }
}