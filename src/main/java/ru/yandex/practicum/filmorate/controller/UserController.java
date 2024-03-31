package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.info("Добавление пользователя {}", user.getName());
        userService.addUser(user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws UserNotFoundException {
        log.info("Обновление данных пользователя {}", user.getName());
        userService.updateUser(user);
        log.info("Данные пользователя обновлены");
        return user;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        List<User> users = userService.getAllUsers();
        log.info("Список пользователей получен клиентом");
        return users;
    }

    @DeleteMapping()
    public User deleteUser(long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        User userToDelete = userService.deleteUser(userId);
        log.info("Пользователь удален");
        return userToDelete;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) throws UserNotFoundException {
        log.info("Запрос на получение пользователя с id: {}", userId);
        User user = userService.getUserById(userId);
        log.info("Пользователь с указанным id получен клиентом");
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long userId,
                          @PathVariable Long friendId) throws UserNotFoundException {
        log.info("Пользователь {} добавляет в друзья {}", userId, friendId);
        User friend = userService.getUserById(friendId);
        userService.addFriend(userId, friendId);
        log.info("Добавление в друзья произошло успешно");
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable Long userId,
                             @PathVariable Long friendId) throws UserNotFoundException {
        log.info("Запрос на удаление друга {} пользователем {}", friendId, userId);
        userService.deleteFriend(userId, friendId);
        log.info("Пользователь {} удален из списка друзей {}", friendId, userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllUsersFriends(@PathVariable Long userId) throws UserNotFoundException {
        log.info("Запрашивается список друзей пользователя {}", userId);
        List<User> users = userService.getUsersFriends(userId);
        log.info("Список друзей пользователя {} получен", userId);
        return users;
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable Long userId,
                                       @PathVariable Long friendId) throws UserNotFoundException {
        log.info("Запрос на получение списка общих друзей пользователей {} и {}", userId, friendId);
        List<User> userList = userService.getMutualFriends(userId, friendId);
        log.info("Список общих друзей пользователей {} и {} получен клиентом", friendId, userId);
        return userList;
    }
}
