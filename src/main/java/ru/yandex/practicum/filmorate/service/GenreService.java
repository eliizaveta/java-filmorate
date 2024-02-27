package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

@Service
public class GenreService {
    public final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

}
