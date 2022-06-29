package ru.yandex.group.filmorate.storage.genre;

import ru.yandex.group.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();

    Genre getById(int id);
}
