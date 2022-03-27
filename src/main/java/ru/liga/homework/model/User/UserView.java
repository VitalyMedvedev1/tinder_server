package ru.liga.homework.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private Long id;
    private String username;
    private String name;
    private String sex;
    private String header;
    private String find;
    private String description;
}