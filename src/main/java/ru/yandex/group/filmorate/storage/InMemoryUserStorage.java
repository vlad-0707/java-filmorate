package ru.yandex.group.filmorate.storage;

import org.springframework.stereotype.Component;
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
    @Override
    public Optional<User> findUsersById(long id){
        return Optional.ofNullable(users.get(id));
    }

}
