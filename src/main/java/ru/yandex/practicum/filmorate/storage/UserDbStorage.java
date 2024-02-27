package ru.yandex.practicum.filmorate.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.String.format;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    private UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM users", new UserMapper()));
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setFriends(new HashSet<>());
        jdbcTemplate.update("INSERT INTO users (email , login , name , birthday) VALUES (? , ? , ? , ?)",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return jdbcTemplate.queryForObject(format("SELECT * FROM users WHERE email='%s'", user.getEmail()), new UserMapper());
    }

    @Override
    public User changeUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (getUserById(user.getId()).getFriends() != null) {
            user.setFriends(getUserById(user.getId()).getFriends());
        } else {
            user.setFriends(new HashSet<>());
        }
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? WHERE user_id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(int id) {
        userExistenceCheck(id);
        return jdbcTemplate.queryForObject(format("SELECT * FROM users WHERE user_id=%d", id), new UserMapper());
    }

    public void userExistenceCheck(int id) {
        try {
            jdbcTemplate.queryForObject(format("SELECT * FROM users WHERE user_id=%d", id), new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate(),
                    new HashSet<>());
        }
    }
}
