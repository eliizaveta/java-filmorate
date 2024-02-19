package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    public UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        if (userStorage.getUserId(id) != null) {
            if (userStorage.getUserId(friendId) != null) {
                userStorage.getUserId(id).getFriends().add(friendId);
                userStorage.getUserId(friendId).getFriends().add(id);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    public void deleteFriend(int id, int friendId) {
        User bosUser = userStorage.getUserId(id);
        User notFriend = userStorage.getUserId(friendId);
        if (bosUser != null) {
            if (notFriend != null) {
                if (userStorage.getUserId(id).getFriends().remove(friendId)) {
                    userStorage.getUserId(friendId).getFriends().remove(id);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    public List<User> getListUser(int id) {
        User user = userStorage.getUserId(id);
        if (user != null) {
            if (user.getFriends().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return user.getFriends()
                    .stream()
                    .map(u -> userStorage.getUserId(u))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<User> getMutualFriends(int id, int otherId) {
        User user = userStorage.getUserId(id);
        User friend = userStorage.getUserId(otherId);
        Set<Integer> userFriends;
        if (user != null) {
            if (friend != null) {
                userFriends = new HashSet<>(user.getFriends());
                userFriends.retainAll(friend.getFriends());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userFriends.stream()
                .map(userId -> userStorage.getUserId(userId))
                .collect(Collectors.toList());
    }
}
