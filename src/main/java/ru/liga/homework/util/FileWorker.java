package ru.liga.homework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import ru.liga.homework.constant.Values;
import ru.liga.homework.exception.CustomIOFileException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class FileWorker {

    public String saveUserFormOnDiscAndReturnPath(BufferedImage image, Long userTgId) {
        try {
            log.debug("Save form on disc for userName {}", userTgId);
            File file = new File(Values.USER_DIR + Values.FILE_DIR + new Date().getTime() + "_" + userTgId + "." + Values.FILE_EXT);
            ImageIO.write(image, Values.FILE_EXT, file);
            return file.getName();
        } catch (IOException e) {
            throw new CustomIOFileException("Error when save form for userName " + userTgId + "\n" + e.getMessage());
        }
    }

    public String getUserFormInBase64Format(String fileName) {

        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(Values.USER_DIR + Values.FILE_DIR + fileName));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new CustomIOFileException("Error when get userName FORM from path: " + e.getMessage());
        }
    }

    //TODO Переделал NIO
    public void deleteFileFromDisc(String fileName) {
        log.debug("Delete form file with name: {}", fileName);
        Path filePath = Paths.get(Values.USER_DIR + Values.FILE_DIR + fileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new CustomIOFileException("Error delete file" + Values.USER_DIR + Values.FILE_DIR + fileName);
        }
    }
}
