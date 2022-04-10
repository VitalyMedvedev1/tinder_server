package ru.liga.homework.model.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LookingFor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

    private Long id;
    private Long usertgid;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String header;


    @Enumerated(EnumType.STRING)
    private LookingFor lookingFor;
    private String formFileName;
    private String attachBase64Code;

//    @JsonIgnore
//    private Set<UserView> likes;
//    @JsonIgnore
//    private Set<UserView> likeBy;

    private String description;
    private String loveSign;
}