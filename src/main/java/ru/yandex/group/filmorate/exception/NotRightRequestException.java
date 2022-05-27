package ru.yandex.group.filmorate.exception;

public class NotRightRequestException extends RuntimeException{

    public NotRightRequestException(String message) {
        super(message);
    }
}
