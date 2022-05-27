package ru.yandex.group.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Identifier;
import ru.yandex.group.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {

    Identifier identifier = new Identifier();
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long id) {
        User user = users.get(id);
        if(user == null){
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден");
        }
        return user;
    }

    @Override
    public User create(User user) {
        if (user == null){
            throw new ValidationException("Пользователь не добавлен");
        }
        validateUser(user);
        user.setId(identifier.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user){
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с id:" + user.getId() + " не найден");
        }
        validateUser(user);
        users.put(user.getId(),user);
        return user;
    }

    @Override
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

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

}
