package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List getAllUsers();

    public User addUser(User user);

    public User changeUser(User user);

    public User getUserId(int id);
}
