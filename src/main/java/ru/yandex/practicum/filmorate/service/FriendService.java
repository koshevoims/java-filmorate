package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendService {

    void addFriend(Long userId, Long friendId) throws UserNotFoundException;

    List<User> getFriends(Long userId) throws UserNotFoundException;

    void deleteFriend(Long userId, Long friendId) throws UserNotFoundException;

    List<User> getMutualFriends(Long userId1, Long userId2) throws UserNotFoundException;
}
