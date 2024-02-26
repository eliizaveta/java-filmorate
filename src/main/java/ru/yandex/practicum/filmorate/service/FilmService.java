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
    public final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(int id, int userId) {
        if (userId < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        Film film = filmStorage.getFilmById(id);
        film.getLikes().add(userId);
    }

    public void deleteLikeFilm(int id, int userId) {
        if (userId < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count < 1) {
            throw new ValidationException("Слишком малое число. Count должен быть хотя бы 1, а не " + count);
        }
        return filmStorage.getAllFilms()
                .stream()
                .sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
