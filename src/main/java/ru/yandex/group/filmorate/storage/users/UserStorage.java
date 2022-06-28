package ru.yandex.group.filmorate.storage.users;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.group.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;


public interface UserStorage {
    List<User> findUsers();

    User create(@Valid @RequestBody User user);

    User update(@Valid @RequestBody User user);

    User findUsersById(long id);

    void delete(User user);
}
