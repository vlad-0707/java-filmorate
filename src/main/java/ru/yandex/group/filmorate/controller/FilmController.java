package ru.yandex.group.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.Objects;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    private Map<Integer, Film> filmMap = new HashMap<>();

    @GetMapping
    public Map<Integer, Film> findFilms() {
        return filmMap;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        log.info("Фильм {} добавлен!", film);
        filmMap.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateFilm(film);
        log.info("Фильм {} обновлен!", film);
        filmMap.replace(film.getId(), film);
        return film;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            throw new ValidationException("Название у фильма должно быть");
        } else if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Слишком длинное описание");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("День рождения кино 28 декабря 1895 года, раньше фильмов не знаем");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
