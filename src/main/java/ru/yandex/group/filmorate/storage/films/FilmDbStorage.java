package ru.yandex.group.filmorate.storage.films;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.exception.FilmNotFoundException;
import ru.yandex.group.filmorate.exception.ValidationException;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int ratingId = resultSet.getInt("mpa_id");
        String sqlQuery = "SELECT mpa_id, name FROM mpa WHERE mpa_id = ?";
        Mpa rating = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, ratingId);
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(rating)
                .build();
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

    private void checkFilm(long id) {
        String sqlQuery = "SELECT film_id FROM films WHERE film_id = ?";
        SqlRowSet row = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!row.next()) {
            log.warn("Фильм не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public Film getFilmById(long id) {
        checkFilm(id);
        String sqlQuery = "SELECT film_id, name, description, release_date, duration, mpa_id FROM films WHERE " +
                "film_id = ?";
        SqlRowSet row = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (row.next()) {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } else {
            log.warn("Фильм не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT film_id, name, description, release_date, duration, mpa_id FROM films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement prs = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            prs.setString(1, film.getName());
            prs.setString(2, film.getDescription());
            prs.setDate(3, (java.sql.Date.valueOf(film.getReleaseDate())));
            prs.setInt(4, film.getDuration());
            prs.setInt(5, film.getMpa().getId());
            return prs;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        checkFilm(film.getId());
        String sqlQuery = "UPDATE films SET name = ?," +
                " description = ?," +
                " release_date = ?," +
                " duration = ?," +
                " mpa_id = ?" +
                " WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void delete(Film film) {
        checkFilm(film.getId());
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }
}
