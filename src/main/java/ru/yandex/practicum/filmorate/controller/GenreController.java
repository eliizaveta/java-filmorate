package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getAllGenre() {
        log.info("Получим список всех жанров");
        return genreService.genreDao.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreId(@PathVariable int id) {
        log.info("Получим жанр с id " + id);
        return genreService.genreDao.getGenreId(id);
    }
}
