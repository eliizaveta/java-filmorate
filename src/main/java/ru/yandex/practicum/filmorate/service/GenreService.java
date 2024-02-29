package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

@Service
@AllArgsConstructor
public class GenreService {

    public final GenreDao genreDao;
}
