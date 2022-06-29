package ru.yandex.group.filmorate.storage.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.group.filmorate.exception.UserNotFoundException;
import ru.yandex.group.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void checkUser(Long id) {
        String sqlQuery = "SELECT user_id FROM users WHERE user_id = ?";
        SqlRowSet row = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!row.next()) {
            log.warn("Такого пользователя не существует");
            throw new UserNotFoundException("Такого пользователя не существует");
        }
    }

    @Override
    public void add(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        jdbcTemplate.update("MERGE INTO friendship f KEY(user_id, friend_id) VALUES (?, ?)", userId, friendId);
    }

    @Override
    public void delete(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        jdbcTemplate.update("DELETE FROM friendship WHERE friend_id = ? AND user_id = ?", friendId, userId);
    }

    @Override
    public User getById(Long userId) {
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        if (row.next()) {
            return new User(row.getLong("user_id"),
                    row.getString("email"),
                    row.getString("login"),
                    row.getString("name"),
                    Objects.requireNonNull(row.getDate("birthday")).toLocalDate());
        } else {
            log.warn("Такого пользователя не существует");
            throw new UserNotFoundException("Такого пользователя не существует");
        }
    }

    @Override
    public List<User> getAll(Long userId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet row = jdbcTemplate
                .queryForRowSet("SELECT * FROM users" +
                        " WHERE user_id IN" +
                        " (SELECT friend_id FROM friendship" +
                        " WHERE user_id = ?)", userId);
        while (row.next()) {
            friends.add(new User(row.getLong("user_id"),
                    row.getString("email"),
                    row.getString("login"),
                    row.getString("name"),
                    Objects.requireNonNull(row.getDate("birthday")).toLocalDate()));
        }
        return friends;
    }

    @Override
    public List<User> getCommon(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        List<User> friends = new ArrayList<>();
        SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT * FROM users u WHERE u.user_id IN" +
                " (SELECT friend_id FROM friendship fr WHERE fr.user_id = ?)" +
                " AND u.user_id IN (SELECT friend_id FROM friendship fr WHERE fr.user_id = ?)", userId, friendId);
        while (srs.next()) {
            friends.add(new User(srs.getLong("user_id"),
                    srs.getString("email"),
                    srs.getString("login"),
                    srs.getString("name"),
                    Objects.requireNonNull(srs.getDate("birthday")).toLocalDate()));
        }
        return friends;
    }
}
