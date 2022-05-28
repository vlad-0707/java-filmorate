package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.NotRightRequestException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.storage.FilmStorage;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms(){
        return filmStorage.findFilms();
    }

    public void createFilm(Film film){
        validateFilm(film);
        filmStorage.create(film);
        log.info("Фильм {} добавлен",film.getName());
    }

    public void updateFilm(Film film){
        if(film.getId() <= 0) {
            throw new FilmNotFoundException("id не может быть отрицательный или равным 0");
        }
        validateFilm(film);
        filmStorage.update(film);
        log.info("Фильм {} обновлен", film.getName());
    }

    public Film deleteFilm(Film film){
        log.info("Фильм {} удален",film.getName());
        return filmStorage.delete(film);
    }

    public Optional<Film> getFilms(Long id) {
        Film film = filmStorage.findFilmById(id).orElseThrow(() -> new FilmNotFoundException("Фильм не найден"));
        log.info("Фильм {} найден", film.getName());
        return filmStorage.findFilmById(id);
    }

    public void addLike(Long id, Long userId) {
        filmStorage.findFilmById(id).map(x -> {x.addLike(userId); return true;})
                   .orElseThrow(()-> new FilmNotFoundException("id не может быть отрицательным!"));
        log.info("Фильму {} добавлен лайк", filmStorage.findFilmById(id).map(Film::getName));
    }

    public void deleteLike(Long id, Long userId) {
        if (id < 0 || userId < 0) {
            throw new FilmNotFoundException("id не может быть отрицательным!");
        }
        filmStorage.findFilmById(id).map(x -> {x.deleteLike(userId); return true;})
                   .orElseThrow(()-> new FilmNotFoundException("id не может быть отрицательным!"));
        log.info("У фильма {} удален лайк", filmStorage.findFilmById(id).map(Film::getName));
    }

    public List<Film> getPopularFilms(Long count) {
        List<Film> popularFilm = filmStorage.findFilms().stream()
                .sorted((s1, s2) -> s2.getFilmLikes().size() - s1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());

        log.info("Самые популярные фильмы {}",popularFilm);
        return popularFilm;
    }
    private void validateFilm(Film film) throws ValidationException {
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            throw new ValidationException("Название у фильма должно быть");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("День рождения кино 28 декабря 1895 года, раньше фильмов не знаем");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
