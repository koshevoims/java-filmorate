package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getUsersFriends(Long userId);

    List<User> getMutualFriends(Long userId1, Long userId2);

    public User addUser(User user);

    public User updateUser(User user);

    public List<User> getAllUsers();

    public User deleteUser(long userId);

    public User getUserById(long userId);
}
