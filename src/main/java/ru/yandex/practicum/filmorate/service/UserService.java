package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    public final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        if (friendId < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        userStorage.getUserById(id).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.getUserById(id).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(id);
    }

    public List<User> getListUser(int id) {
        User user = userStorage.getUserById(id);
        List<User> friends = new ArrayList<>();
        for (int friendId : user.getFriends()) {
            for (User u : userStorage.getAllUsers()) {
                if (u.getId() == friendId) {
                    friends.add(u);
                }
            }
        }
        return friends;
    }

    public List<User> getMutualFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();
        Set<Integer> result = new HashSet<>();
        for (int u : userFriends) {
            for (int f : friendFriends) {
                if (u == f) {
                    result.add(u);
                }
            }
        }
        return result.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
