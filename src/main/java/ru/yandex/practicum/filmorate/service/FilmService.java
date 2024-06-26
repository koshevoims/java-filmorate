package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getTopRatedFilms(Integer count);

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public List<Film> getAllFilms();

    public void deleteFilm(long filmId);

    public Film getFilmById(long filmId);
}
