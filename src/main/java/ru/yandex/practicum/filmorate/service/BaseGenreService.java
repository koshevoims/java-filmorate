package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class BaseGenreService implements GenreService {

    private GenreStorage genreStorage;

    @Autowired
    public BaseGenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre addGenre(Genre genre) {
        return genreStorage.addGenre(genre);
    }

    @Override
    public Genre updateGenre(Genre genre) {
        return genreStorage.updateGenre(genre);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    @Override
    public Genre deleteGenre(int genreId) {
        return genreStorage.deleteGenre(genreId);
    }

    @Override
    public Genre getGenreById(int genreId) throws GenreNotFoundException {
        Genre genre = genreStorage.getGenreById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Жанр с id " + genreId + " не найден"));
        return genre;
    }
}
