package ru.yandex.group.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Identifier;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    Identifier identifier = new Identifier();

    @Override
    public ArrayList<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        film.setId(identifier.getId());
        return films.put(film.getId(), film);
    }

    @Override
    public Film update(Film film) {
        validateFilm(film);
        return films.put(film.getId(), film);
    }

    @Override
    public Film delete(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public Film findFilmsById(long id) {
        return films.get(id);
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