package ru.liga.homework.model.Like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeView {

    private Long id;
    private String usernamefirst;
    private String usernamesecond;
    private Boolean userfirstflg;
    private Boolean usersecondflg;
}