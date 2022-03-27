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
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public void authorization(@RequestBody UserView userView) {
        userService.createUser(userView);
    }

    @GetMapping("/user")
    public UserView create(@RequestParam(name = "username") String userName) {
        return userService.findUserByName(userName);
    }
}