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
    private HashMap<Integer, Film> films = new HashMap<>();
    private int nextFilmId = 1;

    @Override
    public List<Film> getAllFilms() { // получение списка всех фильмов
        return new ArrayList(films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException { // создание фильма.
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
        } else {
            film.setId(nextFilmId++);
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film changeFilm(Film film) throws ValidationException { // обновление фильма.
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
            } else {
                films.put(film.getId(), film);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return film;
    }

    @Override
    public Film getFilmById(int id) { //вернуть фильм по id
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return films.get(id);
    }
}
