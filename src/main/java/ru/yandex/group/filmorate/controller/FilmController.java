package ru.yandex.group.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.service.FilmService;



import javax.validation.Valid;
import java.util.*;

@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        if(id <= 0) {
            throw new FilmNotFoundException("id не может быть отрицательный или равным 0");
        }
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        filmService.createFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if(film.getId() <= 0) {
            throw new FilmNotFoundException("id не может быть отрицательный или равным 0");
        }
        filmService.updateFilm(film);
        return film;
    }

    @DeleteMapping
    public Film deleteFilm(@RequestBody Film film) {
        if (film.getId() <= 0){
            throw new FilmNotFoundException("id не может быть отрицательный или равным 0");
        }
        return filmService.deleteFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        if (id < 0 || userId < 0) throw new ValidationException("id не может быть отрицательным!");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        if (id < 0 || userId < 0) {
            throw new FilmNotFoundException("id не может быть отрицательным!");
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopularFilms(count);
    }
}
