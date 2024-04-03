package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(Long userId, Long friendId) throws UserNotFoundException;

    void deleteFriend(Long userId, Long friendId) throws UserNotFoundException;

    List<User> getUsersFriends(Long userId) throws UserNotFoundException;

    List<User> getMutualFriends(Long userId1, Long userId2) throws UserNotFoundException;

    public User addUser(User user);

    public User updateUser(User user) throws UserNotFoundException;

    public List<User> getAllUsers();

    public User deleteUser(long userId);

    public User getUserById(long userId) throws UserNotFoundException;
}
