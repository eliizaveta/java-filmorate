package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film changeFilm(Film film);

    Film getFilmById(int id);

    void addFilmGenres(int filmId, Set<Genre> genres);

    void updateFilmGenres(int filmId, Set<Genre> genres);

    Set<Genre> getFilmGenres(int filmId);
}
