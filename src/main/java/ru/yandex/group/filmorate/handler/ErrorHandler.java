package ru.yandex.group.filmorate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handler400(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({UserNotFoundException.class, FilmNotFoundException.class})
    public ResponseEntity<?> handler404(Exception e) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> handler500(final Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
