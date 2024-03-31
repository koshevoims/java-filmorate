package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;


import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping()
    public List<Genre> getAll() {
        log.info("Вывод всех жанров");
        List<Genre> genres = genreService.getAllGenres();
        log.info("Список всех жанров выведен");
        return genres;
    }

    @GetMapping("/{genreId}")
    public Genre getById(@PathVariable int genreId) throws GenreNotFoundException {
        log.info("Поиск жанра по его идентификатору: {}", genreId);
        try {
            Genre genre = genreService.getGenreById(genreId);
            log.info("Жанр по идентификатору {} получен! Это {}", genreId, genre.getName());
            return genre;
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException("Жанр с указанным в запросе id не найден");
        }
    }
}
