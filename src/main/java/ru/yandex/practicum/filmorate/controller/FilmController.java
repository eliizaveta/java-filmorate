package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List getAllFilms() {
        log.info("Получим список всех фильмов");
        return filmService.filmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmId(@PathVariable Integer id) {
        log.info("Получим фильм по id " + id);
        return filmService.filmStorage.getFilmById(id);
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавим новый фильм");
        return filmService.filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Изменим информацию про фильм " + film);
        return filmService.filmStorage.changeFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Поставим лайк фильму с id " + id + " пользователем c id " + userId);
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Удалим лайк фильму с id " + id + " пользователем c id " + userId);
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        log.info("Получим список популярных фильмов");
        return filmService.getPopularFilms(count);
    }
}
