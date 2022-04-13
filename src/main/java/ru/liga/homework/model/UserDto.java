package ru.liga.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.liga.homework.repository.entity.User;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;

import java.util.Set;

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
    private String formTitle;
    private LoveSearch loveSearch;
    private String formFileName;
    private String attachBase64Code;
    private String formDescription;
    private String loveSign;

    private Set<User> likes;
    private Set<User> likeBy;
}