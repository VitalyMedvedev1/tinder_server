package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.homework.constant.Values;
import ru.liga.homework.util.form.UsersForm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UsersFormGeneratorTest {

    private static final Long USER_TG_ID = 99999L;

    @Autowired
    private UsersForm usersForm;

    @Test
    void createEmptyUserForm() {
        String pathForm = usersForm.createUserForm(99999L, "", "");
        try (FileInputStream fileInputStream = new FileInputStream(Values.USER_DIR + Values.FILE_DIR + pathForm)) {
            assertNotNull(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(Values.USER_DIR + Values.FILE_DIR + pathForm);
        boolean delete = file.delete();

        assertTrue(delete);
    }

    @Test
    void createUserForm() {
        String pathForm = usersForm.createUserForm(USER_TG_ID, "HEADER", "The following examples show how to use java.awt.image.BufferedImage#flush() ." +
                " These examples are extracted from open source projects. " +
                "You can vote up the ones you like or vote down the ones you don't like, " +
                "and go to the original project or source file by following the links above each example. " +
                "You may check out the related API usage on the sidebar.");
        try (FileInputStream fileInputStream = new FileInputStream(Values.USER_DIR + Values.FILE_DIR + pathForm)) {
            assertNotNull(fileInputStream);
        } catch (IOException ignored) {
        }

        File file = new File(Values.USER_DIR + Values.FILE_DIR + pathForm);
        boolean delete = file.delete();

        assertTrue(delete);
    }
}