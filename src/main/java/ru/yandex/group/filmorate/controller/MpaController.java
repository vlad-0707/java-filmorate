package ru.yandex.group.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.group.filmorate.exception.RatingNotFoundException;
import ru.yandex.group.filmorate.model.Mpa;
import ru.yandex.group.filmorate.service.MpaService;

import java.util.List;

@RequestMapping("/mpa")
@RestController
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> getAllRatings() {
        return mpaService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Mpa getRatingById(@PathVariable int id) throws RatingNotFoundException {
        return mpaService.getRating(id);
    }
}

