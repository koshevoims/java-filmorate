package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendService {

    void addFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getMutualFriends(Long userId1, Long userId2);
}
