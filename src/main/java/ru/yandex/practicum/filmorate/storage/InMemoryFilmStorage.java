package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static HashMap<Integer, Film> films = new HashMap<>();
    private static int nextFilmId = 1;

    private static void incrementId() {
        nextFilmId += 1;
    }

    @Override
    public List<Film> getAllFilms() { // получение списка всех фильмов
        return new ArrayList(films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Вы указали неверную дату. Фильм не может быть старше 1895.12.28");
        } else {
            film.setId(nextFilmId);
            incrementId();
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film changeFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Вы указали неверную дату. Фильм не может быть старше 1895.12.28");
            } else {
                films.put(film.getId(), film);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли фильм, чтобы изменить");
        }
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли фильм по id");
        }
        return films.get(id);
    }
}
