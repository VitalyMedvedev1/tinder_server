package ru.liga.homework.api;

import java.awt.image.BufferedImage;

public interface UsersFormService {
    String createUserForm(String userName, String header, String description);

    String saveUserFormOnDiscAndReturnPath(BufferedImage image, String userName);

    String getUserFormInBase64Format(String fileName);
}