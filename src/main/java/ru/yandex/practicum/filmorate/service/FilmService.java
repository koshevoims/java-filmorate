package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long id;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.id = 1;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getId(long id) {
        log.info("Получили запрос. Фильм id: {}", id);
        return filmStorage.getId(id);
    }

    public Film create(Film film) {
        if (!validCheck(film)) {
            log.error("дата релиза — не раньше 28 декабря 1895 года: {}", film.toString());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        film.setId(++id);
        log.info("Сохраняем фильм: {}", film.toString());
        filmStorage.save(film);
        return film;
    }

    public Film change(Film film) {
        if (!validCheck(film)) {
            log.error("дата релиза — не раньше 28 декабря 1895 года: {}", film.toString());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        var oldFilm = filmStorage.getId(film.getId());
        log.info("Обновляем фильм: {}", film.toString());
        if (oldFilm == null) {
            log.error("Фильм не существует: {}", film.toString());
            throw new ObjectNotFoundException(String.format("POST /films: Фильм не существует id %d not found ", film.getId()));
        }
        return filmStorage.save(film);
    }

    public Film addLike(long id, long userId) {
        var film = filmStorage.getId(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("PUT like: film id %d не найден", id));
        }
        var user = userStorage.getId(userId);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("PUT like: user id %d не найден", userId));
        }
        film.getLikes().add(userId);
        return null;
    }

    public Film deleteLike(long id, long userId) {
        var film = filmStorage.getId(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("PUT like: film id %d not found", id));
        }
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getTop(Integer count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Boolean validCheck(Film film) {
        return !film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"));
    }
}
