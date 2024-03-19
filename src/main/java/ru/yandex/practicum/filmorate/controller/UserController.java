package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Component
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> getAll() {
        log.info("Получить всех пользователей. Количество пользователей");
        List<User> users = userService.getAll();
        log.info("Возвращаем ответ. Количество пользователей: {}", users.size());
        return users;
    }

    @GetMapping("/users/{id}") //TO DO - Validation ID
    public User getId(@PathVariable Integer id) {
        log.info("Получили пользователя по id. Пользователь id: {}", id);
        User user = userService.getId(id);
        log.info("Возвращаем ответ. Пользователь : {}", user);
        return user;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Создаем нового  пользователя {}", user);
        User createdUser = userService.create(user);
        log.info("Сохранили пользователя {}", createdUser);
        return createdUser;
    }

    @PutMapping("/users")
    public User change(@Valid @RequestBody User user) {
        log.info("Изменить пользователя {}", user);
        User savedUser = userService.change(user);
        log.info("Изменить пользователя {}", savedUser);
        return savedUser;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getSharedFriend(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getSharedFriends(id, otherId);
    }
}
