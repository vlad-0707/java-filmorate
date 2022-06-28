package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.User;
import ru.yandex.group.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.group.filmorate.storage.users.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public void deleteUser(User user) {
        userStorage.delete(user);
    }

    public User getUserById(Long id) {
        return userStorage.findUsersById(id);
    }

    public List<User> getUsers() {
        return userStorage.findUsers();
    }

    public void addToUsersFriend(Long userId, Long friendId) {
        friendshipStorage.add(userId, friendId);
    }

    public void deleteFromUsersFriend(Long userId, Long friendId) {
        friendshipStorage.delete(userId, friendId);
    }

    public List<User> getFriend(Long id) {
        return friendshipStorage.getAll(id);
    }

    public List<User> getUsersFriend(Long userId, Long friendId) {
        return friendshipStorage.getCommon(userId, friendId);
    }
}
