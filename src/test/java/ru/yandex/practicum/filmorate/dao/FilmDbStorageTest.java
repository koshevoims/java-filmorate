package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.IncorrectGenreException;
import ru.yandex.practicum.filmorate.exception.IncorrectMpaException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Nested
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    void addFilmTest() throws IncorrectMpaException, IncorrectGenreException {
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, mpaDbStorage, genreDbStorage);
        Film film = Film.builder()
                .id(1L)
                .name("someName")
                .description("someDescription")
                .releaseDate(LocalDate.of(2021, 2, 4))
                .duration(120L)
                .mpa(mpaDbStorage.getMpaById(1))
                .genres(genreDbStorage.getAllGenres())
                .build();
        filmDbStorage.addFilm(film);
        assertThat(filmDbStorage).isNotNull();
        assertEquals(1, filmDbStorage.getAllFilms().size());
        assertEquals(film, filmDbStorage.getFilmById(film.getId()));
    }

    @Test
    void updateFilmTest() throws IncorrectMpaException, IncorrectGenreException {
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, mpaDbStorage, genreDbStorage);
        Film film = Film.builder()
                .id(2L)
                .name("someName")
                .description("someDescription")
                .releaseDate(LocalDate.of(2000, 2, 4))
                .duration(120L)
                .mpa(mpaDbStorage.getMpaById(1))
                .genres(genreDbStorage.getAllGenres())
                .build();
        filmDbStorage.addFilm(film);
        Film filmToUpdate = Film.builder()
                .id(2L)
                .name("someName1")
                .description("someDesc2")
                .releaseDate(LocalDate.of(1990, 3, 12))
                .duration(123L)
                .mpa(mpaDbStorage.getMpaById(3))
                .genres(genreDbStorage.getGenresByFilmId(1L))
                .build();
        filmDbStorage.updateFilm(filmToUpdate);
        assertThat(filmDbStorage.getFilmById(film.getId())).isNotEqualTo(filmDbStorage.getFilmById(film.getId()));
    }

    @Test
    void deleteFilmTest() throws IncorrectMpaException, IncorrectGenreException {
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, mpaDbStorage, genreDbStorage);
        Film film = Film.builder()
                .id(3L)
                .name("someName2")
                .description("someDescription2")
                .releaseDate(LocalDate.of(2000, 2, 4))
                .duration(120L)
                .mpa(mpaDbStorage.getMpaById(1))
                .genres(genreDbStorage.getAllGenres())
                .build();
        filmDbStorage.addFilm(film);

        //System.out.println(filmDbStorage.getFilmById(1));
    }
}
