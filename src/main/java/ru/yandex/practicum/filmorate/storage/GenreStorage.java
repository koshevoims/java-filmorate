package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    public Genre addGenre(Genre genre);

    public Genre updateGenre(Genre genre);

    public List<Genre> getAllGenres();

    public Genre deleteGenre(int genreId);

    public Optional<Genre> getGenreById(int genreId);

    public List<Genre> getGenresByFilmId(Long filmId);
}
