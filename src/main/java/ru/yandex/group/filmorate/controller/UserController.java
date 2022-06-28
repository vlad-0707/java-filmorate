package ru.yandex.group.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

import org.springframework.web.bind.annotation.*;
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
    public List<User> findUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User findUserId(@PathVariable Long id) {
        return userService.getUserById(id);
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
    public void addFriends(@PathVariable Long id,
                           @PathVariable Long friendId) {
        userService.addToUsersFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Long id,
                              @PathVariable Long friendId) {
        userService.deleteFromUsersFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        return userService.getFriend(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getAllCommonFriendsById(@PathVariable Long id,
                                              @PathVariable Long otherId) {
        return userService.getUsersFriend(id, otherId);
    }
}
