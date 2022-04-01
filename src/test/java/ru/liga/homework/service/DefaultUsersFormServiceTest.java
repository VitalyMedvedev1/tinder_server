package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LookingFor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DefaultUsersFormServiceTest {

    @Autowired
    private UsersFormService usersFormService;

    @Test
    void createUserForm() {

        UserView userView = new UserView(1L, "12312312", "Вася", "1123", Gender.MALE, "HEADER", LookingFor.ALL,
                "The following examples show how to use java.awt.image.BufferedImage#flush() ." +
                        " These examples are extracted from open source projects. " +
                        "You can vote up the ones you like or vote down the ones you don't like, " +
                        "and go to the original project or source file by following the links above each example. " +
                        "You may check out the related API usage on the sidebar.");

        usersFormService.createUserForm(userView);
        assertThat(true);
    }
}