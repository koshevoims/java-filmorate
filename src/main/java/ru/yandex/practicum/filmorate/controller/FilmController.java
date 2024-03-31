package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film)
            throws MpaNotFoundException, IncorrectMpaException, IncorrectGenreException {
        log.info("Добавляю фильм " + film.getName());
        filmService.addFilm(film);
        log.info("Фильм " + film.getName() + " добавлен; его id: " + film.getId());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFoundException {
        log.info("Обновляю информацию о фильме " + film.getName());
        filmService.updateFilm(film);
        log.info("Информация о фильме обновлена");
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Вывожу список всех фильмов");
        List<Film> allFilms = filmService.getAllFilms();
        log.info("Список всех фильмов выведен");
        return allFilms;
    }

    @DeleteMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(long filmId) {
        log.info("Удаляю фильм с идентификатором " + filmId);
        filmService.deleteFilm(filmId);
        log.info("Фильм с id " + filmId + " удалён");
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable long filmId) throws FilmNotFoundException {
        log.info("Получаю фильм по идентификатору " + filmId);
        Film film = filmService.getFilmById(filmId);
        log.info("Фильм с указанным идентификатором получен");
        return film;
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void setLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
        log.info("Пользователь " + userId + " поставил отметку like фильму " + filmId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) throws FilmNotFoundException,
            UserNotFoundException {
        Film film = filmService.getFilmById(filmId);
        filmService.deleteLike(filmId, userId);
        log.info("Пользователь " + userId + " убрал отметку like с фильма " + filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopRatedFilms(@RequestParam(required = false) Integer count) {
        log.info("Получен запрос на составление списка " + count + " самых оцениваемых фильмов");
        List<Film> topRatedFims = filmService.getTopRatedFilms(count);
        log.info("Список фильмов с наибольшим количеством оценок получен");
        return topRatedFims;
    }

}

