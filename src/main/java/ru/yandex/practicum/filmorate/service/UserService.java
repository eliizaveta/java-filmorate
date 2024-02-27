package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.FriendListDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    public final UserStorage userStorage;
    public FriendListDao friendListDao;

    public UserService(UserStorage userStorage, FriendListDao friendListDao) {
        this.userStorage = userStorage;
        this.friendListDao = friendListDao;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User changeUser(User user) {
        userStorage.userExistenceCheck(user.getId());
        if (user.getFriends() != null) {
            for (int friendId : user.getFriends()) {
                boolean status = userStorage.getUserById(friendId).getFriends().contains(user.getId());
                friendListDao.addFriends(user.getId(), friendId, status);
            }
        }
        return userStorage.changeUser(user);
    }

    public void addFriend(int id, int friendId) {
        if (friendId < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не нашли пользователя по id");
        }
        userStorage.userExistenceCheck(id);
        boolean status = userStorage.getUserById(friendId).getFriends().contains(id);
        friendListDao.addFriends(id, friendId, status);
    }

    public void deleteFriend(int id, int friendId) {
        friendListDao.deleteFriends(id, friendId);
    }

    public List<User> getListUser(int id) {
        List<User> friends = new ArrayList<>();
        for (int friendId : friendListDao.getFriends(id)) {
            for (User u : userStorage.getAllUsers()) {
                if (u.getId() == friendId) {
                    friends.add(u);
                }
            }
        }
        return friends;
    }

    public List<User> getMutualFriends(int id, int friendId) {
        return friendListDao.getFriends(id).stream()
                .filter(friendListDao.getFriends(friendId)::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
