package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectGenreException;
import ru.yandex.practicum.filmorate.exception.IncorrectMpaException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;


@Service
public class BaseFilmService implements FilmService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    private LikeService likeService;


    @Override
    public void addLike(Long filmId, Long userId) {
        likeService.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        likeService.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        List<Film> resultList;
        resultList = filmStorage.getTopRatedFilms(count);
        return resultList;
    }


    public Film addFilm(Film film) throws IncorrectMpaException, MpaNotFoundException, IncorrectGenreException {
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) throws FilmNotFoundException {
        filmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void deleteFilm(long filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }
}
