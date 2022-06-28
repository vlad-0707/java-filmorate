package ru.yandex.group.filmorate.storage.film_genre;

import java.util.TreeSet;

public interface FilmGenreStorage {
    TreeSet<Integer> getById(Long id);

    void add(Long id, int genreId);

    void delete(Long id);
}
