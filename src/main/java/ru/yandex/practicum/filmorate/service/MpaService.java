package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

@Service
@AllArgsConstructor
public class MpaService {

    public MpaDao mpaDao;
}
