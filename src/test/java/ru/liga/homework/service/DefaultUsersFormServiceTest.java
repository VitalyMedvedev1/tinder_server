package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.homework.api.UsersFormService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DefaultUsersFormServiceTest {

    private static final String FILE_DIR = "/forms/";
    private static final String USER_DIR = System.getProperty("user.dir");

    @Autowired
    private UsersFormService usersFormService;

    @Test
    void createEmptyUserForm() {
        String pathForm = usersFormService.createUserForm(-1, "", "");
        try (FileInputStream fileInputStream = new FileInputStream(USER_DIR + FILE_DIR + pathForm)) {
            assertNotNull(fileInputStream);
        } catch (IOException e) {}

        File file = new File(USER_DIR + FILE_DIR + pathForm);
        boolean delete = file.delete();

        assertTrue(delete);
    }

    @Test
    void createUserForm() {
        String pathForm = usersFormService.createUserForm(-1, "HEADER", "The following examples show how to use java.awt.image.BufferedImage#flush() ." +
                " These examples are extracted from open source projects. " +
                "You can vote up the ones you like or vote down the ones you don't like, " +
                "and go to the original project or source file by following the links above each example. " +
                "You may check out the related API usage on the sidebar.");
        try (FileInputStream fileInputStream = new FileInputStream(USER_DIR + FILE_DIR + pathForm)) {
            assertNotNull(fileInputStream);
        } catch (IOException ignored) {}

        File file = new File(USER_DIR + FILE_DIR + pathForm);
/*        boolean delete = file.delete();

        assertTrue(delete);*/
    }
}