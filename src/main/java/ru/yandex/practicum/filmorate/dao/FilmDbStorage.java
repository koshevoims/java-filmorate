package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectGenreException;
import ru.yandex.practicum.filmorate.exception.IncorrectMpaException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
public class FilmDbStorage implements FilmStorage, RowMapper<Film> {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }


    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        if (!mpaStorage.getAllMpa().contains(film.getMpa())) {
            throw new IncorrectMpaException("Рейтинг, указанный в фильме, не найден");
        }
        if (!film.getGenres().isEmpty()) {
            List<Genre> allGenre = genreStorage.getAllGenres();
            for (Genre genre : film.getGenres()) {
                if (!allGenre.contains(genre)) {
                    throw new IncorrectGenreException("Жанр фильма не найден");
                }
            }
        }
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        for (Genre genre : film.getGenres()) {
            String sqlQuery = "INSERT INTO FILMGENRE(FILM_ID, GENRE_ID) VALUES(?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            genre.setName(genreStorage.getGenreById(genre.getId()).getName());
        }
        return film;
    }

    @Override
    public void deleteFilm(long filmId) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET FILM_TITLE = ?,"
                            + " FILM_DESCRIPTION = ?,"
                            + " FILM_RELEASE_DATE = ?,"
                            + " FILM_DURATION = ?,"
                            + " FILM_MPA_ID = ?"
                            + " WHERE FILM_ID = ?";
        int updateCount = jdbcTemplate.update(sqlQuery,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        if (updateCount != 0) {
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            return film;
        } else {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public Film getFilmById(long filmId) {
        try {
            String sqlQuery = "SELECT FILM_ID,"
                                + " FILM_TITLE,"
                                + " FILM_DESCRIPTION,"
                                + " FILM_RELEASE_DATE,"
                                + " FILM_DURATION,"
                                + " FILM_MPA_ID"
                                + " FROM FILMS"
                                + " WHERE FILM_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        String sqlQuery = "SELECT FILMS.*"
                            + " FROM FILMS"
                            + " INNER JOIN LIKES ON FILMS.FILM_ID = LIKES.FILM_ID"
                            + " GROUP BY FILMS.FILM_ID"
                            + " ORDER BY COUNT(LIKES.FILM_ID)"
                            + " DESC limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRow, count);
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) {
        try {
            Film film = Film.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .description(rs.getString(3))
                    .releaseDate(rs.getDate(4).toLocalDate())
                    .duration(rs.getLong(5))
                    .mpa(mpaStorage.getMpaById(rs.getInt(6)))
                    .build();
            LinkedHashSet<Genre> genres = new LinkedHashSet<>();
            genres.addAll(genreStorage.getGenresByFilmId(film.getId()));
            String likesQuery = "SELECT USER_ID"
                                + " FROM LIKES"
                                + " JOIN FILMS ON LIKES.FILM_ID = FILMS.FILM_ID"
                                + " WHERE FILMS.FILM_ID = ?";
            film.setLikes(jdbcTemplate.queryForList(likesQuery, Long.class, film.getId()));
            film.setGenres(genres);
            return film;
        } catch (SQLException e) {
            throw new FilmNotFoundException("Фильм не найден");
        }

    }
}
