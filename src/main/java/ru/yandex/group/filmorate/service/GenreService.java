package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.GenreNotFoundException;
import ru.yandex.group.filmorate.model.Genre;
import ru.yandex.group.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(int id) throws GenreNotFoundException {
        Genre g = genreStorage.getById(id);
        if (Objects.isNull(g)) {
            log.warn("Такого жанра не существует");
            throw new GenreNotFoundException("Такого жанра не существует");
        }
        return g;
    }

    public List<Genre> getGenresList() {
        return genreStorage.getAll();
    }
}
