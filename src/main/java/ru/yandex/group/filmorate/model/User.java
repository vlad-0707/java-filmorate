package ru.yandex.group.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
@Data
public class User {

    public User() {
        friendsID = new HashSet<>();
    }

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    @Pattern(regexp = "^\\S*$")
    private LocalDate birthday;
    private Set<Long> friendsID;
}
