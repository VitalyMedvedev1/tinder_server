package ru.liga.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.liga.homework.model.UserDto;
import ru.liga.homework.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping("/{username}")
    public UserDto find(@PathVariable("username") Long userTgId) {
        return userService.findByTgId(userTgId);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    @PostMapping("/{username1}/likes/{username2}")
    public void like(@PathVariable("username1") Long userTgIdWhoLike,
                     @PathVariable("username2") Long userTgIdWhoWasLike) {
        userService.like(userTgIdWhoLike, userTgIdWhoWasLike);
    }

    @GetMapping("/{username}/favorite")
    public List<UserDto> findFavorites(@PathVariable("username") Long userTgId) {
        return userService.findFavorites(userTgId);
    }

    @GetMapping
    public Page<UserDto> findUsersWithPageable(@RequestParam(value = "username") Long userTgId,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "size") int size) {
        return userService.findUsersWithPageable(userTgId, page, size);
    }
}