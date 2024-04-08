package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage, RowMapper<User>, FriendService {

    private final JdbcTemplate jdbcTemplate;

    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        if (user.getName().length() < 1) {
            user.setName(user.getLogin());
        }
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE USERS"
                            + " SET USER_EMAIL = ?,"
                            + " USER_LOGIN = ?,"
                            + " USER_NAME = ?,"
                            + " USER_BIRTHDAY = ?"
                            + " WHERE USER_ID = ?";
        int updateCount = jdbcTemplate.update(sqlQuery,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (updateCount != 0) {
            return user;
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT USERS.USER_ID,"
                            + " USERS.USER_EMAIL,"
                            + " USERS.USER_LOGIN,"
                            + " USERS.USER_NAME,"
                            + " USERS.USER_BIRTHDAY"
                            + " FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public User getUserById(long userId) throws UserNotFoundException {
        try {
            String sqlQuery = "SELECT USER_ID,"
                                + " USER_EMAIL,"
                                + " USER_LOGIN,"
                                + " USER_NAME,"
                                + " USER_BIRTHDAY"
                                + " FROM USERS"
                                + " WHERE USER_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) throws UserNotFoundException {
        try {
            String sqlQuery = "INSERT INTO FRIENDSHIPS(USER_ID, FRIEND_ID) VALUES(?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getFriends(Long userId) throws UserNotFoundException {
        try {
            String test = "SELECT U.*"
                            + " FROM FRIENDSHIPS AS FS"
                            + " LEFT JOIN USERS AS U ON U.USER_ID = FS.FRIEND_ID"
                            + " WHERE FS.USER_ID = ?";
            return jdbcTemplate.query(test, this::mapRow, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) throws UserNotFoundException {
        try {
            String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getMutualFriends(Long userId1, Long userId2) throws UserNotFoundException {
        try {
            String sqlQuery = "SELECT U.* FROM FRIENDSHIPS AS F1"
                                + " INNER JOIN FRIENDSHIPS AS f2 ON f1.FRIEND_ID = f2.FRIEND_ID"
                                + " AND f1.USER_ID = ? and f2.USER_ID = ?"
                                + " INNER JOIN USERS AS U ON U.USER_ID = f2.FRIEND_ID";
            return jdbcTemplate.query(sqlQuery, this::mapRow, userId1, userId2);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) {
        try {
            User user = User.builder()
                    .id(rs.getLong(1))
                    .email(rs.getString(2))
                    .login(rs.getString(3))
                    .name(rs.getString(4))
                    .birthday(rs.getDate(5).toLocalDate())
                    .build();
            return user;
        } catch (SQLException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
