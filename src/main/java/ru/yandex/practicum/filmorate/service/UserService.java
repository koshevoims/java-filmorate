package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private long id;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.id = 0;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public User create(User user) {
        if (!validCheck(user)) {
            throw new ValidationException("POST /users: логин не может содержать пробелы");
        }
        user.setId(++id);
        log.info("Сохраняем пользователя: {}", user.toString());
        return userStorage.save(user);
    }

    public User change(User user) {
        if (!validCheck(user)) {
            throw new ValidationException("POST /users: логин не может содержать пробелы");
        }
        var oldUser = userStorage.getById(user.getId());
        if (oldUser == null) {
            throw new ObjectNotFoundException(String.format("PUT /users: Пользователь с id %d  не найден", user.getId()));
        }
        return userStorage.save(user);
    }

    public User addFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("PUT friends: id одинаковые");
        }
        var user = userStorage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("PUT friends: Пользователь id %d не найден", id));
        }
        var friend = userStorage.getById(friendId);
        if (friend == null) {
            throw new ObjectNotFoundException(String.format("PUT friends: friend id %d не найден", friendId));
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("DELETE friends: id одинаковые");
        }
        var user = userStorage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("DELETE friends: user id %d не найден", id));
        }
        var friend = userStorage.getById(friendId);
        if (friend == null) {
            throw new ObjectNotFoundException(String.format("DELETE friends: friend id %d не найден", friendId));
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        return user;
    }

    public List<User> getFriends(Long id) {
        var user = userStorage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("DELETE friends: user id %d not found", id));
        }
        List<User> friends = new ArrayList<>();
        for (var friendId: user.getFriends()) {
            friends.add(userStorage.getById(friendId));
        }
        return friends;
    }

    public List<User> getSharedFriends(Long id, Long otherId) {
        var user = userStorage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("DELETE friends: user id %d не найден", id));
        }
        var otherUser = userStorage.getById(otherId);
        if (otherUser == null) {
            throw new ObjectNotFoundException(String.format("DELETE friends: other id %d не найден", id));
        }
        var commonIds = new HashSet<>(user.getFriends());
        commonIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (var friendId: commonIds) {
            commonFriends.add(userStorage.getById(friendId));
        }
        return commonFriends;
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
