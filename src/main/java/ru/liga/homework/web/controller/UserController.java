package ru.liga.homework.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
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
    public UserView findByName(@PathVariable("username") Long userTgId) {
        return userService.find(userTgId);
    }

    @PutMapping("/users")
    public UserView update(@RequestBody UserView userView) {
        return userService.update(userView);
    }

    @GetMapping("/users/{username1}/likes/{username2}")
    public void like(@PathVariable("username1") Long userTgIdWhoLike,
                     @PathVariable("username2") Long userTgIdWhoWasLike) {
        userService.like(userTgIdWhoLike, userTgIdWhoWasLike);
    }

    @GetMapping("/users/{username}/favorite")
    public void findFavorites(@PathVariable("username") Long userTgId) {
        userService.findFavorites(userTgId);
    }

    @GetMapping("/users")
    public Page<UserView> findUsersWithPageable(@RequestParam(value = "username") Long userTgId,
                                            @RequestParam(value = "page") int page,
                                            @RequestParam(value = "size") int size) {
        return userService.findUsersWithPageable(userTgId, page, size);
    }
}