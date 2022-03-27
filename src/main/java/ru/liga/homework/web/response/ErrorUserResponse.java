package ru.liga.homework.web.response;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ErrorUserResponse {

    private String level = "ERROR";
    private String message;

    public ErrorUserResponse(String message) {
        this.message = message;
    }

}
