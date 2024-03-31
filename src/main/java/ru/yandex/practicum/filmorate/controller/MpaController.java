package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<RatingMpa> getAll() {
        log.info("Получен запрос на вывод всех MPA-рейтингов");
        List<RatingMpa> allMpas = mpaService.getAllMpa();
        log.info("Список всех MPA-рейтингов получен");
        return allMpas;
    }

    @GetMapping("/mpa/{mpaId}")
    public RatingMpa getById(@PathVariable int mpaId) throws MpaNotFoundException {
        log.info("Поиск MPA-рейтинга по идентификатору " + mpaId);
        try {
            RatingMpa mpa = mpaService.getMpaById(mpaId).get();
            log.info("Mpa-рейтинг по идентификатору " + mpaId + " получен! Это " + mpa.getName());
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException("Рейтинг с указанным в запросе id не найден");
        }
    }
}
