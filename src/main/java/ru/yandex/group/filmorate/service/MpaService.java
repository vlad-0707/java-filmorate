package ru.yandex.group.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.group.filmorate.exception.RatingNotFoundException;
import ru.yandex.group.filmorate.model.Mpa;
import ru.yandex.group.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getRating(int id) throws RatingNotFoundException {
        Mpa m = mpaStorage.getById(id);
        if (Objects.isNull(m)) {
            log.warn("Такого рейтинга не существует");
            throw new RatingNotFoundException("Такого рейтинга не существует");
        }
        return m;
    }

    public List<Mpa> getAllRatings() {
        return mpaStorage.getAll();
    }
}
