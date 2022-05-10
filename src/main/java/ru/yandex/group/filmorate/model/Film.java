package ru.yandex.group.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @PastOrPresent
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
