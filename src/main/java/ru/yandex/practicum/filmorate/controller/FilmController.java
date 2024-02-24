package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 0;

    @GetMapping()
    public List<Film> getAll() {
        log.info("Получили запрос. Количество фильмов: " + films.size());
        return List.copyOf(films.values());
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        if (validCheck(film)) {
            id++;
            film.setId(id);
            log.info("Сохраняем фильм: " + film.toString());
            films.put(film.getId(), film);
            return film;
        } else return film;
    }

    @PutMapping()
    public Film put(@RequestBody Film film) {
        if (validCheck(film)) {
            if (films.containsKey(film.getId())){
                log.info("Обновляем фильм: " + film.toString());
                films.put(film.getId(), film);
            } else {
                log.info("Фильм не существует: " + film.toString());
                throw new ValidationException("Фильма не существует не существует");
            }
            return film;
        }
        return film;
    }

    private Boolean validCheck(Film film) {
        if (film.getName().isBlank()){
            log.info("название не может быть пустым: " + film.toString());
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() >= 200){
            log.info("максимальная длина описания — 200 символов: " + film.toString());
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))){
            log.info("дата релиза — не раньше 28 декабря 1895 года: " + film.toString());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0){
            log.info("продолжительность фильма должна быть положительной: " + film.toString());
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
