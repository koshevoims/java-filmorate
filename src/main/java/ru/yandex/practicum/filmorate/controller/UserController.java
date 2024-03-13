package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private Integer id = 0;

    @GetMapping()
    public List<User> getUserAll() {
        log.info("Получили запрос. Количество пользователей: {}", users.size());
        return List.copyOf(users.values());
    }

    @GetMapping("/users/{id}")
    public User getUserId(@PathVariable Integer id) {
        log.info("Получили запрос. Пользователь id: {}", id);
        return users.get(id);
    }

    @PostMapping()
    public User postUser(@Valid @RequestBody User user) {
        if (validCheck(user)) {
            user.setId(++id);
            log.info("Сохраняем пользователя: {}", user.toString());
            users.put(user.getId(), user);
            return user;
        }
        return user;
    }

    @PutMapping()
    public User putUser(@Valid @RequestBody User user) {
        if (validCheck(user)) {
            if (users.containsKey(user.getId())) {
                log.info("Обновляем пользователя: {}", user.toString());
                users.put(user.getId(), user);
            } else {
                log.error("Пользователя не существует: {}", user.toString());
                throw new ValidationException("Пользователя не существует");
            }
            return user;
        }
        return user;
    }

    private Boolean validCheck(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("логин не может содержать пробелы: {}", user.toString());
            throw new ValidationException("логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("имя для отображения может быть пустым — в таком случае будет использован логин: {}", user.toString());
            user.setName(user.getLogin());
        }
        return true;
    }
}
