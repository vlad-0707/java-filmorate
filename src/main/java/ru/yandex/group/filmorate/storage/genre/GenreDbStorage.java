package ru.yandex.group.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.exception.GenreNotFoundException;
import ru.yandex.group.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT * FROM genre WHERE id = ?";
        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);
        if (row.next()) {
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } else {
            log.warn("Такого жанра не существует");
            throw new GenreNotFoundException("Такого жанра не существует");
        }
    }
}
