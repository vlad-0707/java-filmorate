package ru.yandex.group.filmorate.storage.films;

import ru.yandex.group.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilmById(long id);

    List<Film> getAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);
}
