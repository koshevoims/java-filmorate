package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    public Genre addGenre(Genre genre);

    public Genre updateGenre(Genre genre);

    public List<Genre> getAllGenres();

    public void deleteGenre(int genreId);

    public Genre getGenreById(int genreId);
}
