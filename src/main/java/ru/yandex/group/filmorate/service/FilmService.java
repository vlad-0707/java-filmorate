package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.storage.FilmStorage;
import java.util.List;
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
        filmStorage.create(film);
        log.info("Фильм {} добавлен",film.getName());
    }

    public void updateFilm(Film film){
        filmStorage.update(film);
        log.info("Фильм {} обновлен", film.getName());
    }

    public Film deleteFilm(Film film){
        log.info("Фильм {} удален",film.getName());
        return filmStorage.delete(film);
    }
    public Film getFilm( Long id) {
       Film film = filmStorage.findFilmsById(id);
       log.info("Фильм {} найден",film.getName());
       return filmStorage.findFilmsById(id);
    }
    public void addLike(Long id, Long userId) {
        filmStorage.findFilmsById(id).addLike(userId);
        log.info("Фильму {} добавлен лайк", filmStorage.findFilmsById(id).getName());
    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.findFilmsById(id).deleteLike(userId);
        log.info("У фильма {} удален лайк", filmStorage.findFilmsById(id).getName());
    }

    public List<Film> getPopularFilms(Long count) {
        List<Film> popularFilm = filmStorage.findFilms().stream()
                .sorted((s1, s2) -> s2.getFilmLikes().size() - s1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());

        log.info("Самые популярные фильмы {}",popularFilm);
        return popularFilm;
    }
}
