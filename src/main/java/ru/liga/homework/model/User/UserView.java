package ru.liga.homework.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LookingFor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

    private Long id;
    private String username;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String header;

    @Enumerated(EnumType.STRING)
    private LookingFor lookingFor;
    private String description;
}