package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List getAllUsers();

    User addUser(User user);

    User changeUser(User user);

    User getUserId(int id);
}
