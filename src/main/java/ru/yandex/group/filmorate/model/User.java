package ru.yandex.group.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    public User(){
        friendsID = new HashSet<>();
    }
    private long id;
    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;

    @Past
    private LocalDate birthday;

    private Set<Long> friendsID;

    public void addToFriends(Long id) {
        friendsID.add(id);
    }
    public void deleteFromFriends(Long id) {
        friendsID.remove(id);
    }


}
