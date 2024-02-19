package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            film.getLikes().add(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLikeFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            if (!film.getLikes().remove(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count < 1) {
            throw new ValidationException("слишком малое число. count должен быть хотябы 1 а не " + count);
        }
        return filmStorage.getAllFilms()
                .stream()
                .sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
