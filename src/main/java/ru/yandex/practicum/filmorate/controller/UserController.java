package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Component
@AllArgsConstructor
@Slf4j
@Validated
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<User> getAll() {
        log.info("Получить всех пользователей. Количество пользователей");
        List<User> users = userService.getAll();
        log.info("Возвращаем ответ. Количество пользователей: {}", users.size());
        return users;
    }

    @GetMapping("/{id}") //TO DO - Validation ID
    public User getById(@PathVariable @Positive Integer id) {
        log.info("Получили пользователя по id. Пользователь id: {}", id);
        User user = userService.getById(id);
        log.info("Возвращаем ответ. Пользователь : {}", user);
        return user;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info("Создаем нового  пользователя {}", user);
        User createdUser = userService.create(user);
        log.info("Сохранили пользователя {}", createdUser);
        return createdUser;
    }

    @PutMapping()
    public User change(@Valid @RequestBody User user) {
        log.info("Изменить пользователя {}", user);
        User savedUser = userService.change(user);
        log.info("Изменить пользователя {}", savedUser);
        return savedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getSharedFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long otherId) {
        return userService.getSharedFriends(id, otherId);
    }
}
