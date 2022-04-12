package ru.liga.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LookingFor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private Long usertgid;
    private String name;
    private String password;
    private Gender gender;
    private String header;
    private LookingFor lookingFor;
    private String formFileName;
    private String attachBase64Code;
    private String description;
    private String loveSign;
}