package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @GetMapping()
    public List<Film> getAll() {
        log.info("Получили запрос. Количество фильмов: {}", films.size());
        return List.copyOf(films.values());
    }

    @GetMapping("/films/{id}")
    public Film getId(@PathVariable Integer id) {
        log.info("Получили запрос. Фильм id: {}", id);
        return films.get(id);
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        if (validCheck(film)) {
            film.setId(++id);
            log.info("Сохраняем фильм: {}", film.toString());
            films.put(film.getId(), film);
            return film;
        } else return film;
    }

    @PutMapping()
    public Film put(@Valid @RequestBody Film film) {
        if (validCheck(film)) {
            if (films.containsKey(film.getId())) {
                log.info("Обновляем фильм: {}", film.toString());
                films.put(film.getId(), film);
            } else {
                log.error("Фильм не существует: {}", film.toString());
                throw new ValidationException("Фильма не существует не существует");
            }
            return film;
        }
        return film;
    }

    private Boolean validCheck(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("дата релиза — не раньше 28 декабря 1895 года: {}", film.toString());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        return true;
    }
}
