package ru.yandex.group.filmorate.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Identifier {
    private long id;
    public long getId() {
        return ++id;
    }
}