package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectGenreException;
import ru.yandex.practicum.filmorate.exception.IncorrectMpaException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmService {
    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getTopRatedFilms(Integer count);
    public Film addFilm(Film film) throws MpaNotFoundException, IncorrectMpaException, IncorrectGenreException;

    public Film updateFilm(Film film) throws FilmNotFoundException;

    public List<Film> getAllFilms();

    public Film deleteFilm(long filmId);

    public Film getFilmById(long filmId) throws FilmNotFoundException;
}
