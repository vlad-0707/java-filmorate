package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.storage.FilmStorage;
import ru.yandex.group.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }
    public List<Film> getAllFilms() {
        return filmStorage.findFilms();
    }

    public void createFilm(Film film) {
        validateFilm(film);
        filmStorage.create(film);
        log.info("Фильм {} добавлен", film.getName());
    }

    public void updateFilm(Film film) {
        getFilmById(film.getId());
        validateFilm(film);
        filmStorage.update(film);
        log.info("Фильм {} обновлен", film.getName());
    }

    public Film deleteFilm(Film film) {
        log.info("Фильм {} удален", film.getName());
        return filmStorage.delete(film);
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.findFilmById(id).orElseThrow(() -> new FilmNotFoundException("Фильм не найден"));
        log.info("Фильм {} найден", film.getName());
        return film;
    }

    public void addLike(Long id, Long userId) {
        getFilmById(id).addLike(userId);
        log.info("У фильма {} удален лайк", getFilmById(id).getName());
    }

    public void deleteLike(long id, long userId) {
        userStorage.findUsersById(userId).orElseThrow(()->new UserNotFoundException("Пользователь не найден"));
        getFilmById(id).deleteLike(userId);
        log.info("У фильма {} удален лайк", getFilmById(id).getName());
    }

    public List<Film> getPopularFilms(Long count) {
        List<Film> popularFilm = filmStorage.findFilms().stream()
                .sorted((s1, s2) -> s2.getFilmLikes().size() - s1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());

        log.info("Самые популярные фильмы {}", popularFilm);
        return popularFilm;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            throw new ValidationException("Название у фильма должно быть");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("День рождения кино 28 декабря 1895 года, раньше фильмов не знаем");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
