package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;

public interface FriendListDao {
    void addFriends(int id, int frendId, boolean status);

    List<Integer> checkFriend(int id);

    void deleteFriends(int id, int friendId);
}
