package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.homework.api.UserService;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.model.User.UserView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DefaultUserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void findUsersWithPageable() {
        List<UserView> listU1 = userService.findUsersWithPageable("USER1", 0, 1);
        List<UserView> listU2 = userService.findUsersWithPageable("USER1", 1, 1);
        List<UserView> listU3 = userService.findUsersWithPageable("USER1", 2, 1);
        List<UserView> listU4 = userService.findUsersWithPageable("USER1", 3, 1);
        System.out.println(123);
    }
}