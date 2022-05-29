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
        getUserById(user.getId());
        validateUser(user);
        userStorage.update(user);
        log.info("Информация о пользователе {} обновлена", user.getLogin());
        return user;
    }

    public User getUserById(Long id){
        User user = userStorage.findUsersById(id)
                .orElseThrow(()-> new UserNotFoundException("Пользователь не найден"));
        log.info("Пользователь найден {}",user.getName());
        return user;
    }

    public List<User> getUsers(){
        return userStorage.findUsers();
    }

    public User addToUsersFriend(Long id, Long friendId){
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.addToFriends(friendId);
        friend.addToFriends(id);

        log.info("Пользователь {} и пользователь {} добавили друг друга в друзья",user.getName(),friend.getName());

        return user;
    }

    public User deleteFromUsersFriend(Long id, Long friendId){
        User user = getUserById(id);
        User friend = getUserById(friendId);

        user.deleteFromFriends(friendId);
        friend.deleteFromFriends(id);

        log.info("Пользователь {} и пользователь {} удалились из друзей",user.getName(),friend.getName());

        return user;
    }

    public List<User> getFriend(Long id) {
        User user = getUserById(id);
        Set<Long> allFriendsID = user.getFriendsID();
        List<User> friends = new ArrayList<>();
        for(Long f : allFriendsID){
            User friend = getUserById(f);
            friends.add(friend);
        }
        log.info("У " + user.getName() + " друзья с id: " + user.getFriendsID());

        return friends;
    }

    public List<User> getUsersFriend(Long id, Long friendId){
        List<User> friendsNew = new ArrayList<>();
        Set<Long> userOne = getUserById(id).getFriendsID();
        Set<Long> userTwo = getUserById(friendId).getFriendsID();
        for(Long f : userOne){
            if (userTwo.contains(f)){
                friendsNew.add(getUserById(f));
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
