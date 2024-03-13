package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class UserControllerTest {
    private Validator validator;
    private List<String> message;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        message = new ArrayList<>();
    }

    @Test
    void testUserCanNotEmptyLogin() {
        User user = new User(0, "email@emil.ru", "", "", LocalDate.now().minusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Login cannot be empty");
    }

    @Test
    void testUserCanEmptyName() {
        User user = new User(0, "email@emil.ru", "Lucky", "", LocalDate.now().minusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
        }
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void testUserCanNotEmptyEmail() {
        User user = new User(0, "", "Lucky", "", LocalDate.now().minusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Email should be");
    }

    @Test
    void testUserValidEmail() {
        User user = new User(0, "jygjhgj", "Lucky", "", LocalDate.now().minusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Email should be valid");
    }

    @Test
    void testUserCanNotNullBithday() {
        User user = new User(0, "email@emil.ru", "Lucky", "", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Birthday cannot be null");
    }

    @Test
    void testUserValidBithday() {
        User user = new User(0, "email@emil.ru", "Lucky", "", LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Birthday cannot be in future");
    }

    @Test
    void testUserCanNotNegativeDuration() {
        User user = new User(0, "email@emil.ru", "Lucky", "", LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            message.add(violation.getMessage());
        }
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(message.get(0), "Birthday cannot be in future");
    }


}