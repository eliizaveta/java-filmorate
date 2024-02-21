package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;

    public List getAllUsers() { // получение списка всех пользователей.
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextUserId++);
        users.put(user.getId(), user);
        return user;
    }

    public User changeUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя, чтобы изменить");
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getUserId(int id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        return users.get(id);
    }
}
