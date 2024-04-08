package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

@Component
public interface FilmStorage {

    public Film addFilm(Film film);

    public void deleteFilm(long filmId);

    public Film updateFilm(Film film);

    public List<Film> getAllFilms();

    public Film getFilmById(long filmId);

    public List<Film> getTopRatedFilms(Integer count);
}
