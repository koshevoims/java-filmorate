package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class BaseMpaService implements MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public BaseMpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public RatingMpa addMpa(RatingMpa mpa) {
        return mpaStorage.addMpa(mpa);
    }

    @Override
    public RatingMpa updateMpa(RatingMpa mpa) {
        return mpaStorage.updateMpa(mpa);
    }

    @Override
    public List<RatingMpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    @Override
    public void deleteMpa(int mpaId) {
        mpaStorage.deleteMpa(mpaId);
    }

    @Override
    public RatingMpa getMpaById(int mpaId) {
        RatingMpa mpa = mpaStorage.getMpaById(mpaId);
        return mpa;
    }
}
