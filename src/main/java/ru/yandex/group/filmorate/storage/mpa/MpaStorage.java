package ru.yandex.group.filmorate.storage.mpa;

import ru.yandex.group.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAll();

    Mpa getById(Integer id);
}
