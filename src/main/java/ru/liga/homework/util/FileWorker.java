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

    public String saveUserFormOnDiscAndReturnPath(BufferedImage image, Long userTgId) {
        try {
            log.debug("Save form on disc for userName {}", userTgId);
            File file = new File(StaticConstant.USER_DIR + StaticConstant.FILE_DIR + new Date().getTime() + "_" + userTgId + "." + StaticConstant.FILE_EXT);
            ImageIO.write(image, StaticConstant.FILE_EXT, file);
            return file.getName();
        } catch (IOException e) {
            log.error("Error when save form for userName {} \n {}", userTgId, e.getMessage());
            throw new BusinessLogicException("Error when save form for userName " + userTgId + "\n" + e.getMessage());
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
            log.debug("Delete form file with name: {}", fileName);
            boolean delete = new File(StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName).delete();
            if (!delete){
                throw new BusinessLogicException("Error delete file" + StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName);
            }
        } catch (Exception e) {
            throw new BusinessLogicException("Error delete file" + StaticConstant.USER_DIR + StaticConstant.FILE_DIR + fileName);
        }
    }
}
