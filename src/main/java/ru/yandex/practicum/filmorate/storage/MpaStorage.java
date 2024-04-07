package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface MpaStorage {

    public RatingMpa addMpa(RatingMpa mpa);

    public RatingMpa updateMpa(RatingMpa mpa);

    public List<RatingMpa> getAllMpa();

    public void deleteMpa(int mpaId);

    public RatingMpa getMpaById(int mpaId);
}
