package ru.yandex.group.filmorate.storage;

import ru.yandex.group.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film delete(Film film);

    List<Film> findFilms();
    Optional<Film> findFilmById(long id);
}
