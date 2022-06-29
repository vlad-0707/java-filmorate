package ru.yandex.group.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.group.filmorate.controller.FilmController;
import ru.yandex.group.filmorate.controller.UserController;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Mpa;
import ru.yandex.group.filmorate.model.User;
import ru.yandex.group.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
class FilmorateApplicationTests {
    private UserController uc;
    private FilmController fc;
    private UserService us;
    private User userOne;
    private User userTwo;
    private User userThree;
    private User userFour;
    private Film filmOne;
    private Film filmTwo;
    private Film filmThree;

    @BeforeEach
    void beforeEach(@Autowired UserController uc) {
        this.uc = uc;
        userOne = User.builder()
                .id(1L)
                .name("Vlad")
                .email("testemail1@mail.ru")
                .login("qwerty1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        userTwo = User.builder()
                .id(2L)
                .name("Tester")
                .email("testemail1@mail.ru")
                .login("qwerty2")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();
    }

    @BeforeEach
    void beforeEach1(@Autowired FilmController fc, @Autowired UserService us) {
        this.fc = fc;
        this.us = us;
        filmOne = Film.builder()
                .id(1L)
                .name("TestFilm")
                .mpa(Mpa.builder().id(5).name("NC-17").build())
                .description("Традегия о тестировщике, который безошибочно написал все тесты")
                .releaseDate(LocalDate.of(1995, 5, 5))
                .duration(17)
                .build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testFindUsersById() {
        uc.createUser(userOne);
        Optional<User> userOptional = Optional.ofNullable(us.getUserById(userOne.getId()));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Vlad")
                );
    }

    @Test
    void testCreateUser() {
        userFour = User.builder()
                .id(4L)
                .name("Tester4")
                .email("testemail4@mail.ru")
                .login("qwerty4")
                .birthday(LocalDate.of(1994, 4, 4))
                .build();
        uc.createUser(userFour);
        List<User> users = us.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() {
        uc.createUser(userOne);
        userOne.setName("Updated");
        assertEquals("Updated", userOne.getName());
        us.delete(userOne);
    }

    @Test
    void testFindAllUsers() {
        uc.createUser(userOne);
        uc.createUser(userTwo);
        List<User> users = us.getUsers();
        assertEquals(3, users.size());
        us.delete(userOne);
        us.delete(userTwo);
    }

    @Test
    void testAddFriend() {
        uc.createUser(userOne);
        uc.createUser(userTwo);
        us.addToUsersFriend(userOne.getId(), userTwo.getId());
        List<User> friendList = us.getFriend(userOne.getId());
        assertEquals(1, friendList.size());
        us.delete(userOne);
        us.delete(userTwo);
    }

    @Test
    void testDeleteFriend() {
        uc.createUser(userOne);
        uc.createUser(userTwo);
        List<User> friendList = us.getFriend(1L);
        uc.deleteFriends(userOne.getId(), userTwo.getId());
        assertEquals(0, friendList.size());
        us.delete(userOne);
        us.delete(userTwo);
    }

    @Test
    void testGetCommonFriends() {
        userThree = User.builder()
                .id(3L)
                .name("Robert")
                .email("robert777@mail.ru")
                .login("rob")
                .birthday(LocalDate.of(1993, 3, 3))
                .build();

        uc.createUser(userOne);
        uc.createUser(userTwo);
        uc.createUser(userThree);

        us.addToUsersFriend(userOne.getId(), userTwo.getId());
        us.addToUsersFriend(userOne.getId(), userThree.getId());
        us.addToUsersFriend(userTwo.getId(), userThree.getId());

        List<User> commonFriends = uc.getAllCommonFriendsById(userOne.getId(), userTwo.getId());
        assertEquals(1, commonFriends.size());

        us.delete(userThree);
        us.delete(userOne);
        us.delete(userTwo);
    }

    @Test
    void testFindFilmById() {
        fc.createFilm(filmOne);
        Optional<Film> filmOptional = Optional.ofNullable(fc.getFilm(filmOne.getId()));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "TestFilm")
                );
        fc.deleteFilm(filmOne.getId());
    }

    @Test
    void testCreateFilm() {
        filmTwo = Film.builder()
                .id(3L)
                .name("Absolutely New Film")
                .mpa(Mpa.builder().id(3).name("PG-13").build())
                .description("Описание абсолютно нового фильма")
                .releaseDate(LocalDate.of(1996, 6, 6))
                .duration(23)
                .build();

        fc.createFilm(filmTwo);
        Collection<Film> films = fc.getAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void testGetAllFilms() {
        fc.createFilm(filmOne);
        Collection<Film> films = fc.getAllFilms();
        assertEquals(1, films.size());
    }

    @Test
    void testDeleteFilm() {
        filmThree = Film.builder()
                .id(4L)
                .name("Absolutely New Film2")
                .mpa(Mpa.builder().id(3).name("PG-13").build())
                .description("Описание абсолютно нового фильма: часть 2")
                .releaseDate(LocalDate.of(1997, 7, 7))
                .duration(24)
                .build();

        fc.createFilm(filmThree);
        fc.deleteFilm(filmThree.getId());
        Collection<Film> films = fc.getAllFilms();
        assertEquals(2, films.size());
    }
}
