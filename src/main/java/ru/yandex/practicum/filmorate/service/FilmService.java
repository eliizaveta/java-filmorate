package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {

    public final FilmStorage filmStorage;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;

    public List<Film> getAllFilms() {
        List<Film> list = filmStorage.getAllFilms();
        list.forEach(film -> {
            film.setGenres(filmStorage.getFilmGenres(film.getId()));
            film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
        });
        return list;
    }

    public Film addFilm(Film film) {
        Film filmAdded = filmStorage.addFilm(film);
        filmStorage.addFilmGenres(filmAdded.getId(), film.getGenres());
        filmAdded.setGenres(filmStorage.getFilmGenres(filmAdded.getId()));
        return filmAdded;
    }

    public Film changeFilm(Film film) {
        Film filmChanged = filmStorage.changeFilm(film);
        filmStorage.updateFilmGenres(filmChanged.getId(), film.getGenres());
        filmChanged.setGenres(filmStorage.getFilmGenres(filmChanged.getId()));
        filmChanged.setMpa(mpaDao.getMpaId(filmChanged.getMpa().getId()));
        return filmChanged;
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(filmStorage.getFilmGenres(id));
        film.setMpa(mpaDao.getMpaId(film.getMpa().getId()));
        return film;
    }

    public void likeFilm(int id, int userId) {
        if (userId < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        likeDao.addLike(id, userId);
    }

    public void deleteLikeFilm(int id, int userId) {
        if (userId < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        likeDao.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Integer> filmsId = likeDao.sizeLikeFilmList(count);
        List<Film> popularFilmList = new ArrayList<>();
        if (filmsId.isEmpty()) {
            return getAllFilms().stream().sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                    .limit(count).collect(Collectors.toList());
        } else {
            for (int filmId : filmsId) {
                popularFilmList.add(filmStorage.getFilmById(filmId));
            }
        }
        return popularFilmList;
    }
}
