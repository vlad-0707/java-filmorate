package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Genre;
import ru.yandex.group.filmorate.storage.film_genre.FilmGenreStorage;
import ru.yandex.group.filmorate.storage.films.FilmStorage;
import ru.yandex.group.filmorate.storage.genre.GenreStorage;
import ru.yandex.group.filmorate.storage.likes_film.LikesFilmStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;
    private final LikesFilmStorage likesFilmStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Autowired
    public FilmService(GenreStorage genreStorage, FilmStorage filmStorage, LikesFilmStorage likesFilmStorage,
                       FilmGenreStorage filmGenreStorage) {
        this.genreStorage = genreStorage;
        this.filmStorage = filmStorage;
        this.likesFilmStorage = likesFilmStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    private void checkUser(Long id) {
        if (id < 0) {
            log.warn("Такого пользователя не существует");
            throw new UserNotFoundException("Такого пользователя не существует");
        }
    }

    private void checkFilm(Long id) {
        if (filmStorage.getFilmById(id) == null) {
            log.warn("Такого фильма не существует");
            throw new FilmNotFoundException("Такого фильма не существует");
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film createFilm(Film film) {
        Film filmToAdd = filmStorage.createFilm(film);
        film.setId(filmToAdd.getId());
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                filmGenreStorage.add(film.getId(), g.getId());
                log.info("Фильм {} добавлен", film.getName());
            }
        }
        return film;
    }

    public Film updateFilm(Film film) {
        checkFilm(film.getId());
        filmGenreStorage.delete(film.getId());
        filmStorage.updateFilm(film);
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                filmGenreStorage.add(film.getId(), g.getId());
                log.info("Фильм {} обновлен", film.getName());
            }
        }
        return film;
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(filmStorage.getFilmById(id));
    }

    public Film getFilmById(Long id) {
        final Film getFilmById = filmStorage.getFilmById(id);
        if (getFilmById == null) {
            log.warn("Такого фильма не существует");
            throw new FilmNotFoundException("Такого фильма не существует");
        } else {
            log.info("Фильм {} найден", getFilmById.getName());
            TreeSet<Integer> idOfGenres = filmGenreStorage.getById(id);
            LinkedHashSet<Genre> genreSet = new LinkedHashSet<>();
            if (!idOfGenres.isEmpty()) {
                for (Integer genre : idOfGenres) {
                    genreSet.add(genreStorage.getById(genre));
                }
                getFilmById.setGenres(genreSet);
            } else {
                getFilmById.setGenres(null);
            }
        }
        return getFilmById;
    }

    public void addLike(Long filmId, Long userId) {
        checkFilm(filmId);
        checkUser(userId);
        likesFilmStorage.addLike(filmId, userId);
        log.info("У фильма {} добавлен лайк", getFilmById(filmId).getName());
    }

    public void deleteLike(long filmId, long userId) {
        checkFilm(filmId);
        checkUser(userId);
        likesFilmStorage.deleteLike(filmId, userId);
        log.info("У фильма {} удален лайк", getFilmById(filmId).getName());
    }

    public List<Film> getPopularFilms(int likes) {
        List<Film> bestFilms = likesFilmStorage.getPopularFilms(likes);
        log.info("Самые популярные фильмы {}", bestFilms);
        return bestFilms.subList(0, bestFilms.size());
    }
}
