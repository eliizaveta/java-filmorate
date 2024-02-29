package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;

public interface FriendListDao {
    void addFriends(int id, int friendId, boolean status);

    List<Integer> getFriends(int id);

    void deleteFriends(int id, int friendId);
}
