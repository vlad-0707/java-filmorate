package ru.yandex.group.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Genre {
    private int id;
    private String name;
}
