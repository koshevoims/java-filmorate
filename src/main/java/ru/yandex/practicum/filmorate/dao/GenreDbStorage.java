package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Repository
public class GenreDbStorage implements GenreStorage, RowMapper<Genre> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre addGenre(Genre genre) {
        String sqlQuery = "INSERT INTO GENRES(GENRE_NAME) VALUES(?)";
        jdbcTemplate.update(sqlQuery, genre.getName());
        return genre;
    }

    @Override
    public Genre updateGenre(Genre genre) {
        String sqlQuery = "UPDATE GENRES SET GENRE_NAME = ? WHERE GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public void deleteGenre(int genreId) {
        String sqlQuery = "DELETE FROM GENRES WHERE GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery, genreId);
    }

    @Override
    public Genre getGenreById(int genreId) {
        try {
            String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM GENRES WHERE GENRE_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException("Жанр с id " + genreId + " не найден");
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        String sqlQuery = "SELECT DISTINCT G.GENRE_ID,"
                            + " G.GENRE_NAME"
                            + " FROM FILMGENRE AS FG"
                            + " LEFT JOIN GENRES AS G ON FG.GENRE_ID = G.GENRE_ID"
                            + " WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRow, filmId);
    }


    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt(1))
                .name(rs.getString(2))
                .build();
    }

}
