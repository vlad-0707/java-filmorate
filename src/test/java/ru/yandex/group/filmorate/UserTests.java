package ru.yandex.group.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Identifier;
import ru.yandex.group.filmorate.model.User;
import ru.yandex.group.filmorate.storage.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserTests {
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(new Identifier());
    private final User user = new User(1,"ironMan@mail.ru","IronMan","Tony Stark", LocalDate.of(1965,4,4),new TreeSet<>());
    @Test
    void creatUserTest(){
        inMemoryUserStorage.create(user);
        assertFalse(inMemoryUserStorage.findUsers().isEmpty(),"Пользователь не найден");
    }
    @Test
    void updateUserTest(){
        User user1 = new User(1,"ironMan@mail.ru","IronVlad","Tony Stark",LocalDate.of(1965,4,4),new TreeSet<>());
        inMemoryUserStorage.create(user);
        inMemoryUserStorage.update(user1);
        assertEquals("IronVlad",inMemoryUserStorage.findUsers().get(1).getLogin(), "Юзер не обновился");
    }
    @Test
    void emailTest() {
        user.setEmail("emailWithout dog");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
    }
    @Test
    void nullLoginTest(){
        user.setLogin("loginWithout space");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
    }
    @Test
    void birthdayFutureTest(){
        user.setBirthday(LocalDate.of(2040,11,11));
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
    }
}
