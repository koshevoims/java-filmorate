package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film save(Film film);

    List<Film> getAll();

    Film getId(Long id);
}
