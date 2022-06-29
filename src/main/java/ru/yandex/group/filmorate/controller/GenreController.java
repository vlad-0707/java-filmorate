package ru.yandex.group.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.group.filmorate.exception.GenreNotFoundException;
import ru.yandex.group.filmorate.model.Genre;
import ru.yandex.group.filmorate.service.GenreService;

import java.util.List;

@RequestMapping("/genres")
@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getGenres() {
        return genreService.getGenresList();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) throws GenreNotFoundException {
        return genreService.getGenreById(id);
    }
}
