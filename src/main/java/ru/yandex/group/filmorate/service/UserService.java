package ru.yandex.group.filmorate.service;

import com.sun.jdi.connect.VMStartException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.User;
import ru.yandex.group.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user){
        User newUser = userStorage.create(user);
        log.info("Пользователь {} добавлен в список.", user.getLogin());
        return newUser;
    }

    public User updateUser(User user){
        userStorage.update(user);
        log.info("Информация о пользователе {} обновлена", user.getLogin());
        return user;
    }

    public User getUserId(Long id){
        User user = userStorage.findUserById(id);
        log.info("Пользователь найден {}",user.getName());
        return userStorage.findUserById(id);
    }

    public List<User> getUsers(){
        return userStorage.findUsers();
    }

    public User addToUsersFriend(Long id, Long friendId){
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);

        user.addToFriends(friendId);
        friend.addToFriends(id);

        log.info("Пользователь {} и пользователь {} добавили друг друга в друзья",user.getName(),friend.getName());

        return user;
    }

    public User deleteFromUsersFriend(Long id, Long friendId){
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);

        user.deleteFromFriends(friendId);
        friend.deleteFromFriends(id);

        log.info("Пользователь {} и пользователь {} удалились из друзей",user.getName(),friend.getName());

        return user;
    }

    public List<User> getFriend(Long id) {
        User user = userStorage.findUserById(id);
        Set<Long> allFriendsID = user.getFriendsID();
        List<User> friends = new ArrayList<>();
        for(Long f : allFriendsID){
            User friend = userStorage.findUserById(f);
            friends.add(friend);
        }
        log.info("У " + user.getName() + " друзья с id: " + user.getFriendsID());

        return friends;
    }

    public List<User> getUsersFriend(Long id, Long friendId){
        List<User> friendsNew = new ArrayList<>();
        Set<Long> userOne = userStorage.findUserById(id).getFriendsID();
        Set<Long> userTwo = userStorage.findUserById(friendId).getFriendsID();
        for(Long f : userOne){
            if (userTwo.contains(f)){
                friendsNew.add(userStorage.findUserById(f));
            }
        }
        return friendsNew;
    }
}
