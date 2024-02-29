package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        log.info("Получим список всех MPA");
        return mpaService.mpaDao.getAllMpa();
    }

    @GetMapping("mpa/{id}")
    public Mpa getMpaId(@PathVariable int id) {
        log.info("Получим MPA c id " + id);
        return mpaService.mpaDao.getMpaId(id);
    }
}
