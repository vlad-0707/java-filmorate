package ru.yandex.group.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Objects;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> userMap = new HashMap<>();

    @GetMapping
    public Map<Integer, User> findUsers() {
        return userMap;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        log.info("Пользователь {} добавлен в список.", user);
        userMap.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        if (userMap.containsKey(user.getId())) {
            User oldUser = userMap.get(user.getId());
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setBirthday(user.getBirthday());
            log.info("Информация о пользователе {} обновлена", user);
        }
        return user;
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail().contains(" ") || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        } else if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            throw new ValidationException("Вы забыли указать имя");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дня рождения не может быть из будущего, только если Вы не Марти Махфлай");
        }
    }
}
