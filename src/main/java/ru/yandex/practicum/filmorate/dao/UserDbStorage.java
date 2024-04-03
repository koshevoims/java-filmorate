package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    public User deleteUser(long userId) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
        return null;
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery = "update USERS set USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?,"
                + "USER_BIRTHDAY = ? where USER_ID = ?";
        int updateCount = jdbcTemplate.update(sqlQuery,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (updateCount != 0) {
            return Optional.of(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select USERS.USER_ID, USERS.USER_EMAIL, USERS.USER_LOGIN, USERS.USER_NAME,"
                + " USERS.USER_BIRTHDAY"
                + " from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public User getUserById(long userId) throws UserNotFoundException {
        try {
            String sqlQuery = "select USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY"
                    + " from USERS where USER_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) throws UserNotFoundException {
        try {
            String sqlQuery = "insert into FRIENDSHIPS(USER_ID, FRIEND_ID) VALUES(?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getFriends(Long userId) throws UserNotFoundException {
        try {
            String test = "select u.*"
                    + " from FRIENDSHIPS AS fs"
                    + " left join USERS as U on U.USER_ID = fs.FRIEND_ID"
                    + " where fs.USER_ID = ?";
            return jdbcTemplate.query(test, this::mapRow, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) throws UserNotFoundException {
        try {
            String sqlQuery = "delete from FRIENDSHIPS where USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getMutualFriends(Long userId1, Long userId2) throws UserNotFoundException {
        try {
            String sqlQuery = "select u.* from FRIENDSHIPS as f1"
                    + " inner join FRIENDSHIPS as f2 on f1.FRIEND_ID = f2.FRIEND_ID "
                    + "and f1.USER_ID = ? and f2.USER_ID = ? "
                    + "inner join USERS as u on u.USER_ID = f2.FRIEND_ID";
            return jdbcTemplate.query(sqlQuery, this::mapRow, userId1, userId2);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong(1))
                .email(rs.getString(2))
                .login(rs.getString(3))
                .name(rs.getString(4))
                .birthday(rs.getDate(5).toLocalDate())
                .build();
        return user;
    }
}
