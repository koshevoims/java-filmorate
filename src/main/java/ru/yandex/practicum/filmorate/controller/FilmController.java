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
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film)
            throws MpaNotFoundException, IncorrectMpaException, IncorrectGenreException {
        log.info("Добавляю фильм: {}", film.getName());
        filmService.addFilm(film);
        log.info("Фильм {} добавлен; его id: {} ", film.getName(), film.getId());
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFoundException {
        log.info("Обновляю информацию о фильме: {} ", film.getName());
        filmService.updateFilm(film);
        log.info("Информация о фильме обновлена");
        return film;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Вывожу список всех фильмов");
        List<Film> allFilms = filmService.getAllFilms();
        log.info("Список всех фильмов выведен");
        return allFilms;
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(long filmId) {
        log.info("Удаляю фильм с идентификатором: {}", filmId);
        filmService.deleteFilm(filmId);
        log.info("Фильм с id {} удалён", filmId);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) throws FilmNotFoundException {
        log.info("Получаю фильм по идентификатору: {}", filmId);
        Film film = filmService.getFilmById(filmId);
        log.info("Фильм с указанным идентификатором получен");
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void setLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
        log.info("Пользователь {} поставил отметку like фильму: {}", userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) throws FilmNotFoundException {
        filmService.getFilmById(filmId);
        filmService.deleteLike(filmId, userId);
        log.info("Пользователь {} убрал отметку like с фильма {}", userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopRatedFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Получен запрос на составление списка {} самых оцениваемых фильмов", count);
        List<Film> topRatedFilms = filmService.getTopRatedFilms(count);
        log.info("Список фильмов с наибольшим количеством оценок получен");
        return topRatedFilms;
    }
}
