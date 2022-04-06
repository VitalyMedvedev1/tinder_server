package ru.liga.homework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.type.StaticConstant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class FileWorker {

    public String saveUserFormOnDiscAndReturnPath(BufferedImage image, String userName) {
        try {
            log.debug("Save form on disc for userName {}", userName);
            File file = new File(StaticConstant.USER_DIR + StaticConstant.FILE_DIR + new Date().getTime() + "." + StaticConstant.FILE_EXT);
            ImageIO.write(image, StaticConstant.FILE_EXT, file);
            return file.getName();
        } catch (IOException e) {
            log.error("Error when save form for userName {} \n {}", userName, e.getMessage());
            throw new BusinessLogicException("Error when save form for userName " + userName + "\n" + e.getMessage());
        }
    }

    public String getUserFormInBase64Format(String fileName) {
        byte[] fileContent;
        try {
            fileContent = FileUtils.readFileToByteArray(new File(StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName));
        } catch (IOException e) {
            log.error("Error when get userName FORM from path: {} {} {}", StaticConstant.USER_DIR, StaticConstant.FILE_DIR, fileName);
            throw new BusinessLogicException("Error when get userName FORM from path: " + e.getMessage());
        }
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public void deleteFileFromDisc(String fileName) {
        try {
            boolean delete = new File(StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName).delete();
            if (!delete){
                throw new BusinessLogicException("Error delete file" + StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName);
            }
        } catch (Exception e) {
            throw new BusinessLogicException("Error delete file" + StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName);
        }
    }
}
