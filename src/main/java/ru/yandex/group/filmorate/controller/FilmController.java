package ru.yandex.group.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Identifier;


import javax.validation.Valid;
import java.util.*;
import java.time.LocalDate;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    Identifier identifier = new Identifier();
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(identifier.getId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен!", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateFilm(film);
        log.info("Фильм {} обновлен!", film);
        films.replace(identifier.getId(), film);
        return film;
    }

    private void validateFilm(Film film) {
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
