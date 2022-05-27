package ru.yandex.group.filmorate.exception;

public class FilmNotFoundException extends RuntimeException{

    public FilmNotFoundException(String message) {
        super(message);
    }
}
