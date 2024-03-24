package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Component
@AllArgsConstructor
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getAll() {
        log.info("Получили запрос. Количество фильмов:");
        List<Film> films = filmService.getAll();
        log.info("Возвращаем ответ. Количество фильмов: {}", films.size());
        return films;
    }

    @GetMapping("/films/{id}")
    public Film getId(@PathVariable @Positive Integer id) {
        log.info("Получили запрос. Фильм id: {}", id);
        Film film = filmService.getId(id);
        log.info("Возвращаем ответ. Фильм: {}", film);
        return film;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создаем новый фильм: {}", film);
        Film savedFilm = filmService.create(film);
        log.info("Сохранили новый фильм: {}", film);
        return savedFilm;
    }

    @PutMapping("/films")
    public Film change(@Valid @RequestBody Film film) {
        log.info("Получили запрос. Фильм: {}", film);
        Film changedFilm = filmService.change(film);
        log.info("Возвращаем ответ. Фильм: {}", film);
        return changedFilm;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        log.info("Поставить Like фильму: ");
        Film likedFilm = filmService.addLike(id, userId);
        log.info("Поставили фильму Like{}", likedFilm);
        return likedFilm;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        log.info("Убрать Like фильму: ");
        Film unlikedFilm = filmService.deleteLike(id, userId);
        log.info("Убрали фильму Like{}", unlikedFilm);
        return unlikedFilm;
    }

    @GetMapping("/films/popular")
    public List<Film> getTop(@RequestParam(defaultValue = "10") @Positive final Integer count) {
        log.info("Показать топ: ");
        List<Film> top = filmService.getTop(count);
        log.info("Показали топ : {}", top);
        return top;
    }
}
