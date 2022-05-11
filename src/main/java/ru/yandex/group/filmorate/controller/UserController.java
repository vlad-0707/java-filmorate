package ru.yandex.group.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.*;

import org.springframework.web.bind.annotation.*;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Identifier;
import ru.yandex.group.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    Identifier identifier = new Identifier();
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> findUsers(){
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(identifier.getId());
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен в список.", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setBirthday(user.getBirthday());
            log.info("Информация о пользователе {} обновлена", user);
        }
        return user;
    }

    private void validateUser(User user) {
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
