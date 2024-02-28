package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

@Service
@RequiredArgsConstructor
public class GenreService {

    public final GenreDao genreDao;
}
