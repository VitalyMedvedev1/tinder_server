package ru.liga.homework.api;

import ru.liga.homework.model.User.UserView;

import java.awt.image.BufferedImage;

public interface UsersFormService {
    void createUserForm(UserView userView);

    String saveUserFormOnDiscAndReturnAbsPath(BufferedImage image, Long userId);
}