package ru.yandex.practicum.filmorate.storage.daoImpl;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;

import java.util.List;

import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        try {
            jdbcTemplate.update("INSERT INTO film_like_list (film_id, user_id)VALUES (?, ?)", filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        try {
            jdbcTemplate.queryForObject(format("SELECT film_id + user_id FROM film_like_list " +
                    "WHERE film_id=%d AND user_id=%d", filmId, userId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        jdbcTemplate.update("DELETE FROM film_like_list WHERE film_id=? AND user_id=?", filmId, userId);
    }

    @Override
    public List<Integer> sizeLikeFilmList(int count) {
        return jdbcTemplate.queryForList(format("SELECT film_id FROM (SELECT film_id, COUNT(user_id) as count_users " +
                "FROM film_like_list GROUP BY film_id) as subquery " +
                "ORDER BY count_users DESC LIMIT %d", count), Integer.class);
    }
}
