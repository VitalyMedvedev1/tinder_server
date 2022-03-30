package ru.liga.homework.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserView userView) {
        userService.create(userView);
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserView findUserByName(@PathVariable("username") String userName) {
        return userService.findUserByName(userName);
    }

    @GetMapping("/user/{username1}/like/{username2}")
    public void like(@PathVariable("username1") String userNameWhoLike,
                         @PathVariable("username2") String userNameWhoWasLike) {
        userService.like(userNameWhoLike, userNameWhoWasLike);
    }


    @GetMapping("/user/{username}/favorite")
    public void findFavorites(@PathVariable("username") String userName) {
        userService.findFavorites(userName);
    }
}