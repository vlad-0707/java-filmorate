package ru.yandex.group.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.group.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


public interface UserStorage {

     List<User> findUsers();

     User findUserById(Long id);

     User create(@Valid @RequestBody User user);

     User update(@Valid @RequestBody User user);

}
