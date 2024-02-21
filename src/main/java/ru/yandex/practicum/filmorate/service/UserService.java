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
                addUserFriend(id, friendId);
                addUserFriend(friendId, id);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти друга по id");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя по id");
        }

    }

    private void addUserFriend(int userId, int friendId) {
        Set<Integer> friendsIds = userStorage.getUserId(userId).getFriends();
        if (friendsIds == null) {
            friendsIds = new HashSet<>();
        }
        friendsIds.add(friendId);
    }

    private void deleteUserFriend(int userId, int friendId) {
        Set<Integer> friendsIds = userStorage.getUserId(userId).getFriends();
        if (friendsIds != null) {
            friendsIds.remove(friendId);
        }
    }

    public void deleteFriend(int id, int friendId) {
        User bosUser = userStorage.getUserId(id);
        User notFriend = userStorage.getUserId(friendId);
        if (bosUser != null) {
            if (notFriend != null) {
                deleteUserFriend(id, friendId);
                deleteUserFriend(friendId, id);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти друга по id");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя по id");
        }

    }

    public List<User> getListUser(int id) {
        User user = userStorage.getUserId(id);
        if (user != null) {
            if (user.getFriends() != null) {
                return user.getFriends()
                        .stream()
                        .map(u -> userStorage.getUserId(u))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public List<User> getMutualFriends(int id, int friendId) {
        User user = userStorage.getUserId(id);
        User friend = userStorage.getUserId(friendId);
        Set<Integer> userFriends = user.getFriends();
        if (userFriends == null) {
            return Collections.emptyList();
        }
        Set<Integer> friendFriends = friend.getFriends();
        if (friendFriends == null) {
            friendFriends = new HashSet<>();
        }
        userFriends.retainAll(friendFriends);
        return userFriends.stream()
                .map(userId -> userStorage.getUserId(userId))
                .collect(Collectors.toList());
    }
}
