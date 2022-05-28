package ru.yandex.group.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.model.Identifier;
import ru.yandex.group.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Identifier identifier;
    private final Map<Long, User> users = new HashMap<>();

    public InMemoryUserStorage(Identifier identifier) {
        this.identifier = identifier;
    }
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
        return users.get(id);
    }

    @Override
    public User create(User user) {
        user.setId(identifier.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user){
        users.put(user.getId(),user);
        return user;
    }

}
