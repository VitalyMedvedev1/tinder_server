package ru.liga.homework.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.liga.homework.api.UserService;
import ru.liga.homework.model.User.UserView;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LikeUserController {

    private final UserService userService;

    @PostMapping("/userl")
    public void authorization(@RequestBody UserView userView) {
        userService.createUser(userView);
    }
}