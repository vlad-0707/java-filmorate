package ru.yandex.group.filmorate.model;

public class Identifier {
    private long id;

    public long getId() {
        return ++id;
    }
}