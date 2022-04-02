package ru.liga.homework.api;

import java.awt.image.BufferedImage;

public interface UsersFormService {
    String createUserForm(Integer userId, String header, String description);

    String saveUserFormOnDiscAndReturnPath(BufferedImage image, Integer userId);

    void saveFileNameInDb(String fileName, Integer userId);

    String getUserFormInBase64Format(Integer userId);
}