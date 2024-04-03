package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Long, Film> films = new HashMap<>();
    private long maxId;

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(long filmId) {
        films.remove(filmId);
    }

    @Override
    public Optional<Film> updateFilm(Film film) throws FilmNotFoundException {
        long filmId = film.getId();
        Film currentFilm = Optional.of(films.get(filmId))
                .orElseThrow(() -> new FilmNotFoundException("Ошибка при обновлении информации о фильме"));
        if (currentFilm != null) {
            currentFilm.setName(film.getName());
            currentFilm.setDescription(film.getDescription());
            currentFilm.setReleaseDate(film.getReleaseDate());
            currentFilm.setDuration(film.getDuration());
        }
        return Optional.of(currentFilm);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long filmId) throws FilmNotFoundException {
        Film currentFilm = films.get(filmId);
        return currentFilm;
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        List<Film> resultList = new ArrayList<>(films.values());
        resultList.sort((o1, o2) -> o2.getLikes().size() - o1.getLikes().size());
        if (count > resultList.size()) {
            return resultList;
        }
        return resultList.subList(0, count);
    }

    private long generateId() {
        return ++maxId;
    }
}
