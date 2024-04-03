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
        String sqlQuery = "insert into RATINGMPA(MPA_NAME) values(?)";
        jdbcTemplate.update(sqlQuery, mpa.getName());
        return mpa;
    }

    @Override
    public RatingMpa updateMpa(RatingMpa mpa) {
        String sqlQuery = "update RATINGMPA set MPA_NAME = ? where MPA_ID = ?";
        jdbcTemplate.update(sqlQuery, mpa.getName(), mpa.getId());
        return mpa;
    }

    @Override
    public List<RatingMpa> getAllMpa() {
        String sqlQuery = "select MPA_ID, MPA_NAME from RATINGMPA";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public RatingMpa deleteMpa(int mpaId) {
        String sqlQuery = "delete from RATINGMPA where MPA_ID = ?";
        jdbcTemplate.update(sqlQuery, mpaId);
        return null;
    }

    @Override
    public RatingMpa getMpaById(int mpaId) throws MpaNotFoundException {
        try {
            String sqlQuery = "select MPA_ID, MPA_NAME from RATINGMPA where MPA_ID = ?";
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
