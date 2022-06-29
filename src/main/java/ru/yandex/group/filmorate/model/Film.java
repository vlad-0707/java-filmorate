package ru.yandex.group.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;
    @NotBlank
    private String name;
    private Mpa mpa;

    @Size(max = 200)
    private String description;

    @PastOrPresent
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private LinkedHashSet<Genre> genres;
    private int rate;
}
