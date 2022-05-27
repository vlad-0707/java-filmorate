package ru.yandex.group.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.group.filmorate.exception.NotRightRequestException;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.User;
import ru.yandex.group.filmorate.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User findUserId(@PathVariable Long id){
        return userService.getUserId(id);
    }
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriends(@PathVariable Long id,
                           @PathVariable Long friendId){
        if (id == null){
            throw new NotRightRequestException("Не корректный id:");
        }
        if (friendId == null){
            throw new NotRightRequestException("Не корректный id:");
        }
       return userService.addToUsersFriend(id, friendId);
    }

    @DeleteMapping ("{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable Long id,
                              @PathVariable Long friendId){
        if (id <= 0){
            throw new ValidationException("id не может быть отрицательный или равным 0");
        }
        if (friendId <= 0){
            throw new ValidationException("id не может быть отрицательный или равным 0");
        }
        return userService.deleteFromUsersFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id){
        if(id == null){
            throw new UserNotFoundException("Такого пользователя с id " + id +" нет");
        }
        return userService.getFriend(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getAllCommonFriendsById(@PathVariable  Long id,
                                              @PathVariable  Long otherId){
        if(id == null){
            throw new UserNotFoundException("Не нашелся пользователь");
        }
        if(otherId == null){
            throw new UserNotFoundException("Не нашелся пользователь");
        }
        return userService.getUsersFriend(id,otherId);
    }
}
