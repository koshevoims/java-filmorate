package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.sql.ResultSet;
import java.util.List;

public interface UserStorage extends FriendService {
    public User addUser(User user);

    public User deleteUser(long userId);

    public User updateUser(User user);

    public List<User> getAllUsers();

    public User getUserById(long userId);

    public User mapRow(ResultSet rs, int rowNum);
}
