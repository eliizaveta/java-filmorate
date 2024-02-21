package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> filmLikes = film.getLikes();
        if (filmLikes == null) {
            filmLikes = new HashSet<>();
        }
        filmLikes.add(userId);
    }

    public void deleteLikeFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> filmLikes = film.getLikes();
        if (filmLikes != null) {
            filmLikes.remove(userId);
        }
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
