package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.User;
import ru.yandex.group.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user){
        validateUser(user);
        User newUser = userStorage.create(user);
        log.info("Пользователь {} добавлен в список.", user.getLogin());
        return newUser;
    }

    public User updateUser(User user){
        if (user.getId()<=0) {
            throw new UserNotFoundException("Пользователь с id:" + user.getId() + " не найден");
        }
        validateUser(user);
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

    public void validateUser(User user) {
        if (user.getEmail().contains(" ") || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дня рождения не может быть из будущего, только если Вы не Марти Макфлай");
        }
    }
}
