package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenre() {
        log.info("вызван метод allGenre - запрос на список всех aGenre");
        return genreService.genreDao.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreId(@PathVariable int id) {
        log.info("вызван метод getGenreId - запрос на Genre c Id " + id);
        return genreService.genreDao.getGenreId(id);
    }
}
