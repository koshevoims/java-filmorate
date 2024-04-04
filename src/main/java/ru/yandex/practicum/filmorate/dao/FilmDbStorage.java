package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
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
    public Film addFilm(Film film) throws IncorrectMpaException, IncorrectGenreException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        if (!mpaStorage.getAllMpa().contains(film.getMpa())) {
            throw new IncorrectMpaException("Рейтинг, указанный в фильме, не найден");
        }
        if (film.getGenres().size() > 0) {
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
            String sqlQuery = "insert into FILMGENRE(FILM_ID, GENRE_ID) values(?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            genre.setName(genreStorage.getGenreById(genre.getId()).get().getName());
        }
        return film;
    }

    @Override
    public void deleteFilm(long filmId) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILMS set FILM_TITLE = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?,"
                + "FILM_DURATION = ?, FILM_MPA_ID = ? where FILM_ID = ?";
        int updateCount = jdbcTemplate.update(sqlQuery,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        if (updateCount != 0) {
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    @Override
    public Film getFilmById(long filmId) throws FilmNotFoundException {
        try {
            String sqlQuery = "select FILM_ID, FILM_TITLE, FILM_DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION,"
                    + "FILM_MPA_ID from FILMS where FILM_ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRow, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        String sqlQuery = "select FILMS.* from FILMS inner join LIKES on FILMS.FILM_ID = LIKES.FILM_ID "
                + "group by FILMS.FILM_ID order by count(LIKES.FILM_ID) desc limit ?";
        return   jdbcTemplate.query(sqlQuery, this::mapRow, count);
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .description(rs.getString(3))
                .releaseDate(rs.getDate(4).toLocalDate())
                .duration(rs.getLong(5))
                .mpa(mpaStorage.getMpaById(rs.getInt(6)))
                .build();
        List<Genre> genres = genreStorage.getGenresByFilmId(film.getId());
        String likesQuery = "select USER_ID from LIKES join FILMS on LIKES.FILM_ID = FILMS.FILM_ID"
                + " where FILMS.FILM_ID = ?";
        film.setLikes(jdbcTemplate.queryForList(likesQuery, Long.class, film.getId()));
        film.setGenres(genres);
        return film;
    }
}
