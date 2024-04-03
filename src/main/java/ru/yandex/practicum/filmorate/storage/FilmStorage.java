package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectGenreException;
import ru.yandex.practicum.filmorate.exception.IncorrectMpaException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {

    public Film addFilm(Film film) throws MpaNotFoundException, IncorrectMpaException, IncorrectGenreException;

    public void deleteFilm(long filmId);

    public Optional<Film> updateFilm(Film film) throws FilmNotFoundException;

    public List<Film> getAllFilms();

    public Film getFilmById(long filmId) throws FilmNotFoundException;

    public List<Film> getTopRatedFilms(Integer count);
}
