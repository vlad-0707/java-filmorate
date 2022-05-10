package ru.yandex.group.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.group.filmorate.controller.FilmController;
import ru.yandex.group.filmorate.controller.UserController;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.User;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

@SpringBootTest
public class UserAndFilmTests {

    private final FilmController filmController = new FilmController();
    private final UserController userController = new UserController();
    private final Film film = new Film(1,"Железный человек","Тони старк: миллиардер, плейбой, филантроп. Во время представления" +
            "очердного оружия попадает в плен, где разрабатывает свой костюм...", LocalDate.of(2008,5,1),128);
    private final User user = new User(1,"ironMan@mail.ru","IronMan","Tony Stark",LocalDate.of(1965,4,4));

    @Test
    void creatFilmTest(){
        filmController.create(film);
        assertFalse(filmController.findFilms().isEmpty(),"Фильм не найден");
    }

    @Test
    void updateFilmTest(){
        Film film1 = new Film(1,"Железный человек2","В этой части железный человек сразится с электрическим утырком" +
                ", смотрите на всех диванах страны", LocalDate.of(2010,4,29),130);
        filmController.create(film);
        filmController.update(film1);
        assertEquals("Железный человек2",filmController.findFilms().get(1).getName(), "Фильм не обновился");
    }

    @Test
    void description200Test(){
        film.setDescription("«Желе́зный челове́к» (англ. Iron Man) — американский супергеройский фильм 2008 года " +
                "режиссёра Джона Фавро с Робертом Дауни-младшим в главной роли. Сценарий написали Арт Маркам и Мэтт " +
                "Холлоуэй и Марк Фергус и Хоук Остби по комиксам Marvel о приключениях супергероя Железного " +
                "человека. Первый фильм в первой фазе кинематографической вселенной Marvel (КВM). Побывав в плену " +
                "у террористов в Афганистане, миллиардер и изобретатель Тони Старк строит высокотехнологичный " +
                "костюм и становится супергероем, известным как Железный человек.\n" +
                "\n" +
                "Работа над кинокомиксом началась в 1990 году. Несколько киностудий, включая Universal Pictures, " +
                "20th Century Fox и New Line Cinema, годами пытались реализовать проект. В 2005 году права на " +
                "экранизацию вернулись в Marvel Studios. В апреле 2006 года Фавро был утверждён в качестве " +
                "режиссёра. Изначально продюсеры Marvel скептически отнеслись к кандидатуре Дауни. В итоге " +
                "актёр подписал контракт в сентябре. Съёмки проводились с марта по июнь 2007 года в Калифорнии. " +
                "Во время съёмок сценарий не был готов и актёрам пришлось импровизировать. Созданием костюма " +
                "Железного человека занималась студия Стэна Уинстона.\n" +
                "\n" +
                "Премьера картины состоялась 14 апреля 2008 года в Сиднее. В прокат в США фильм вышел 2 мая. " +
                "Лента собрала свыше 585 миллионов долларов, став восьмым кассовым фильмом 2008 года. " +
                "Кинокомикс вызвал всеобщее признание критиков. Среди главных достоинств они отмечали " +
                "актёрскую игру Дауни. «Железный человек» был признан Американским институтом кино одним из " +
                "лучших фильмов 2008 года и получил две номинации на премию «Оскар» в категориях «Лучший " +
                "звуковой монтаж» и «Лучшие визуальные эффекты».");
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void durationFilmTest(){
        film.setDuration(-10);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
    @Test
    void dateFilmTest(){
        film.setReleaseDate(LocalDate.of(1650,11,11));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
    @Test
    void creatUserTest(){
        userController.create(user);
        assertFalse(userController.findUsers().isEmpty(),"Пользователь не найден");
    }

    @Test
    void updateUserTest(){
       User user1 = new User(1,"ironMan@mail.ru","IronVlad","Tony Stark",LocalDate.of(1965,4,4));
       userController.create(user);
       userController.update(user1);
       assertEquals("IronVlad",userController.findUsers().get(1).getLogin(), "Юзер не обновился");
    }

    @Test
    void emailTest() {
        user.setEmail("emailWithout dog");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void nullLoginTest(){
        user.setLogin("loginWithout space");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void birthdayFutureTest(){
        user.setBirthday(LocalDate.of(2040,11,11));
        assertThrows(ValidationException.class, () -> userController.create(user));
    }
}
