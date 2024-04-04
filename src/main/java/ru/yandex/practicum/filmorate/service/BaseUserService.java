package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class BaseUserService implements UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Override
    public void addFriend(Long userId1, Long userId2) throws UserNotFoundException {
        userStorage.getUserById(userId1);
        userStorage.getUserById(userId2);
        userStorage.addFriend(userId1, userId2);
    }

    @Override
    public void deleteFriend(Long userId1, Long userId2) throws UserNotFoundException {
        userStorage.getUserById(userId1);
        userStorage.getUserById(userId2);
        userStorage.deleteFriend(userId1, userId2);
    }

    @Override
    public List<User> getMutualFriends(Long userId1, Long userId2) throws UserNotFoundException {
        return userStorage.getMutualFriends(userId1, userId2);
    }

    @Override
    public List<User> getUsersFriends(Long userId) throws UserNotFoundException {
        userStorage.getUserById(userId);
        return userStorage.getFriends(userId);
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User deleteUser(long userId) {
        return userStorage.deleteUser(userId);
    }

    @Override
    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }
}
