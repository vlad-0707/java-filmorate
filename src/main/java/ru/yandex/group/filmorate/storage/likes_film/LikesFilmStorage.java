package ru.yandex.group.filmorate.storage.likes_film;

import ru.yandex.group.filmorate.model.Film;

import java.util.List;

public interface LikesFilmStorage {
    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(int likes);
}

