package ru.yandex.group.filmorate.storage.friendship;

import ru.yandex.group.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void add(Long userId, Long friendId);

    void delete(Long userId, Long friendId);

    User getById(Long userId);

    List<User> getAll(Long userId);

    List<User> getCommon(Long userId, Long friendId);
}
