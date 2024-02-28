package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.daoImpl.GenreDaoImpl;

import javax.validation.ValidationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM films", new FilmMapper()));
    }

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
        }
        jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, rating_id )" +
                        " VALUES(?, ?, ?, ?, ?)",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return jdbcTemplate.queryForObject(format("SELECT * FROM films WHERE name='%s' "
                                + "AND description='%s' "
                                + "AND release_date='%s' "
                                + "AND duration= %d "
                                + "AND rating_id=%d",
                        film.getName(),
                        film.getDescription(),
                        film.getReleaseDate(),
                        film.getDuration(),
                        film.getMpa().getId()),
                new FilmMapper());
    }

    @Override
    public Film changeFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("вы указали неверную дату. фильм не может быть старше 1895.12.28");
        }
        jdbcTemplate.update("UPDATE films "
                        + "SET name=?, description=?, release_date=?, duration=?, rating_id=?"
                        + "WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        try {
            return jdbcTemplate.queryForObject(format("SELECT * FROM films WHERE film_id=%d", id), new FilmMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void addFilmGenres(int filmId, Set<Genre> genres) {
        if (genres != null) {
            final ArrayList<Genre> genreList = new ArrayList<>(genres);
            jdbcTemplate.batchUpdate(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, filmId);
                            ps.setLong(2, genreList.get(i).getId());
                        }
                        public int getBatchSize() {
                            return genreList.size();
                        }
                    }
            );
        }
    }

    @Override
    public void updateFilmGenres(int filmId, Set<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id=?", filmId);
        addFilmGenres(filmId, genres);
    }

    @Override
    public Set<Genre> getFilmGenres(int filmId) {
        return new HashSet<>(jdbcTemplate.query(format(
                "SELECT f.genre_id, g.name "
                        + "FROM film_genres AS f "
                        + "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id "
                        + "WHERE f.film_id=%d "
                        + "ORDER BY g.genre_id", filmId), new GenreDaoImpl.GenreMapper()));
    }

    private class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));

            return new Film(rs.getInt("film_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    new HashSet<>(),
                    mpa,
                    getFilmGenres(rs.getInt("film_id")));
        }
    }
}
