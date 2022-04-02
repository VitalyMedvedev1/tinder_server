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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserView create(@RequestBody UserView userView) {
        return userService.create(userView);
    }

    @GetMapping("/users/{username}")
    public UserView findByName(@PathVariable("username") String userName) {
        return userService.findUser(userName);
    }

    @GetMapping("/users/{username1}/likes/{username2}")
    public void like(@PathVariable("username1") String userNameWhoLike,
                         @PathVariable("username2") String userNameWhoWasLike) {
        userService.like(userNameWhoLike, userNameWhoWasLike);
    }

    @GetMapping("/users/{username}/favorite")
    public void findFavorites(@PathVariable("username") String userName) {
        userService.findFavorites(userName);
    }

    @GetMapping("/users/{username}/favorite")
    public void findAllUser(@RequestParam(value = "username") String userName,
                            @RequestParam(value = "limit") int limit,
                            @RequestParam(value = "offset") int offset) {
        userService.findUsersWithPageable(userName, limit, offset);
    }
}