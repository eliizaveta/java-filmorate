package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

@Service
@RequiredArgsConstructor
public class MpaService {

    public MpaDao mpaDao;
}
