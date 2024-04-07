package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

    @Override
    public void updateFilmGenre(Film film) {
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Genre genre : film.getGenres()) {
            Object[] values = new Object[] {
                    film.getId(), genre.getId()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("INSERT INTO FILMGENRE(FILM_ID, GENRE_ID) VALUES (?, ?)", batch);
    }

    @Override
    public List<Film> injectGenreToFilms(List<Film> films) {
        List<String> fid = films.stream().map(f -> f.getId().toString()).collect(Collectors.toList());
        String args = String.join(", ", fid);
        System.out.println(args);
        String sqlQuery = "SELECT FG.FILM_ID, FG.GENRE_ID, G.GENRE_NAME"
                                + " FROM FILMS AS F"
                                + " LEFT JOIN FILMGENRE AS FG ON F.FILM_ID = FG.FILM_ID"
                                + " LEFT JOIN GENRES AS G ON FG.GENRE_ID = G.GENRE_ID"
                                + " WHERE F.FILM_ID IN (" + args + ")";

        List<Map<Integer, Genre>> result = jdbcTemplate.query(sqlQuery, this::injectMapRow);

        Map<Long, LinkedHashSet<Genre>> maps = new HashMap<>();
        for (Map<Integer, Genre> igm : result) {
          if ((igm.keySet().stream().findFirst().get() != 0)) {
              Integer i = igm.keySet().stream().findFirst().get();
              LinkedHashSet<Genre> lhs = new LinkedHashSet<>();
              if (maps.containsKey(i)) {
                  lhs = maps.get(i);
                  lhs.add(igm.get(i));
                  maps.put(Long.valueOf(i), lhs);
              } else {
                  lhs.add(igm.get(i));
                  maps.put(Long.valueOf(i), lhs);
              }
          }
        }
        for (Film film : films) {
            if (maps.containsKey(film.getId())) {
                film.setGenres(maps.get(film.getId()));
            }
        }
        return films;
    }

    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt(1))
                .name(rs.getString(2))
                .build();
    }

    public Map<Integer, Genre> injectMapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<Integer, Genre> mp = new HashMap<Integer, Genre>();
        mp.put(rs.getInt(1), new Genre(rs.getInt(2), rs.getString(3)));
        return mp;
    }
}
