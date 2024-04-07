package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Repository
public class MpaDbStorage implements MpaStorage, RowMapper<RatingMpa> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RatingMpa addMpa(RatingMpa mpa) {
        String sqlQuery = "INSERT INTO RATINGMPA(MPA_NAME) values(?)";
        jdbcTemplate.update(sqlQuery, mpa.getName());
        return mpa;
    }

    @Override
    public RatingMpa updateMpa(RatingMpa mpa) {
        String sqlQuery = "UPDATE RATINGMPA SET MPA_NAME = ? WHERE MPA_ID = ?";
        jdbcTemplate.update(sqlQuery, mpa.getName(), mpa.getId());
        return mpa;
    }

    @Override
    public List<RatingMpa> getAllMpa() {
        String sqlQuery = "SELECT MPA_ID, MPA_NAME FROM RATINGMPA";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public void deleteMpa(int mpaId) {
        String sqlQuery = "DELETE FROM RATINGMPA WHERE MPA_ID = ?";
        jdbcTemplate.update(sqlQuery, mpaId);
    }

    @Override
    public RatingMpa getMpaById(int mpaId) throws MpaNotFoundException {
        try {
            String sqlQuery = "SELECT MPA_ID, MPA_NAME FROM RATINGMPA WHERE MPA_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException("Mpa не найден");
        }
    }

    @Override
    public RatingMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RatingMpa.builder()
                .id(rs.getInt(1))
                .name(rs.getString(2))
                .build();
    }
}
