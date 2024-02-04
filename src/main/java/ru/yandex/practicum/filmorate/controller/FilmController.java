package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int nextFilmId = 1;

    @GetMapping
    public List getAllFilms() {
        if (films.values().isEmpty()) {
            log.error("Нет фильмов");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка при добавлении фильма " + film + " старше «L'Arrivée d'un train en gare de la Ciotat» 1895 фильмов нет");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            film.setId(nextFilmId++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм " + film.getName());
        }
        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Ошибка при добавлении фильма " + film + " старше «L'Arrivée d'un train en gare de la Ciotat» 1895 фильмов нет");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                films.put(film.getId(), film);
                log.info("Обновлен фильм " + film.getName());
            }
        } else {
            log.error("Не удалось изменить фильм, так как он не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return film;
    }
}
