package ru.yandex.group.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private final Identifier identifier;

    public InMemoryFilmStorage(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public ArrayList<Film> findFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(identifier.getId());
        return films.put(film.getId(), film);
    }

    @Override
    public Film update(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public Film delete(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public Optional<Film> findFilmById(long id) {
        return Optional.ofNullable(films.get(id));
    }

}