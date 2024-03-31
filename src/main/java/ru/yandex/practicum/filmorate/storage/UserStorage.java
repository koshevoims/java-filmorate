package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserStorage extends FriendService {
    public User addUser(User user);

    public User deleteUser(long userId);

    public Optional<User> updateUser(User user) throws UserNotFoundException;

    public List<User> getAllUsers();

    public Optional<User> getUserById(long userId) throws UserNotFoundException;

    public User mapRow(ResultSet rs, int rowNum) throws SQLException;
}
