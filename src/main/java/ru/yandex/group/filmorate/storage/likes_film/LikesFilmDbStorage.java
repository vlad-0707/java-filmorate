package ru.yandex.group.filmorate.storage.likes_film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.model.Film;
import ru.yandex.group.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LikesFilmDbStorage implements LikesFilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesFilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int ratingId = resultSet.getInt("mpa_id");
        String result = "SELECT mpa_id, name FROM mpa WHERE mpa_id = ?";
        Mpa rating = jdbcTemplate.queryForObject(result, this::mapRowToRating, ratingId);

        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(rating)
                .build();
    }

    private Mpa mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes_film (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        String sqlQuery2 = "UPDATE films SET rate = rate + 1";
        jdbcTemplate.update(sqlQuery2);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM likes_film WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        String sqlQuery2 = "UPDATE films SET rate = rate - 1";
        jdbcTemplate.update(sqlQuery2);
    }

    @Override
    public List<Film> getPopularFilms(int likes) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id," +
                " COUNT(l.user_id) AS best FROM films f" +
                " LEFT OUTER JOIN likes_film l ON f.film_id = l.film_id" +
                " GROUP BY f.film_id ORDER BY best DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, likes);
    }
}
