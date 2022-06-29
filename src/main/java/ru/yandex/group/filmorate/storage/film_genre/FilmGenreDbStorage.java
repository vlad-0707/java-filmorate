package ru.yandex.group.filmorate.storage.film_genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TreeSet<Integer> getById(Long id) {
        String sqlQuery = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        List<Integer> genres = jdbcTemplate.queryForList(sqlQuery, Integer.class, id);
        return new TreeSet<>(genres);
    }

    @Override
    public void add(Long id, int genreId) {
        String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, genreId);
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
