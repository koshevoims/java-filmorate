package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    public RatingMpa addMpa(RatingMpa mpa);

    public RatingMpa updateMpa(RatingMpa mpa);

    public List<RatingMpa> getAllMpa();

    public RatingMpa deleteMpa(int mpaId);

    public Optional<RatingMpa> getMpaById(int mpaId);
}
