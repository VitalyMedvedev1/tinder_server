package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import ru.liga.homework.api.UserService;
import ru.liga.homework.model.User.UserView;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DefaultUserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void findUsersWithPageable() {
        Page<UserView> userPage = userService.findUsersWithPageable("user5", 1, 1);
        assertEquals(1, userPage.getSize());
    }

    @Test
    void findUsersWithPageable_1() {
        Page<UserView> userPage = userService.findUsersWithPageable("user5", 0, 4);
        assertEquals(3, userPage.getSize());
    }
}