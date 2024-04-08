package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public Genre addGenre(Genre genre);

    public Genre updateGenre(Genre genre);

    public List<Genre> getAllGenres();

    public void deleteGenre(int genreId);

    public Genre getGenreById(int genreId);

    public List<Genre> getGenresByFilmId(Long filmId);

    public void updateFilmGenre(Film film);

    public List<Film> injectGenreToFilms(List<Film> films);
}
