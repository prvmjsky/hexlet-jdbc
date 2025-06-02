package io.hexlet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private String phone;

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
