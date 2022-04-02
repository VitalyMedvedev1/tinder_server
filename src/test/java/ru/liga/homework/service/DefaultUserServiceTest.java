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
        UserView user1 = userService.findUsersWithPageable("USER2", 0, 1);
        UserView user2 = userService.findUsersWithPageable("USER2", 1, 1);
        UserView user3 = userService.findUsersWithPageable("USER1", 2, 1);
        UserView user4 = userService.findUsersWithPageable("USER1", 3, 1);
        System.out.println(123);
    }
}