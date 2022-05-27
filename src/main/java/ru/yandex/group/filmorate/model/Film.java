package ru.yandex.group.filmorate.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {

    public Film() {
        filmLikes = new HashSet<>();
    }

    public void addLike(Long id) {
        filmLikes.add(id);
    }

    public void deleteLike(Long id) {
        filmLikes.remove(id);
    }
    private Long id;
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @PastOrPresent
    private LocalDate releaseDate;

    @Positive
    private int duration;
    private Set<Long> filmLikes;
}
