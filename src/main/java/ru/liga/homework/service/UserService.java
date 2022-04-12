package ru.liga.homework.service;

import org.springframework.data.domain.Page;
import ru.liga.homework.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto findByTgId(Long userTgId);

    UserDto create(UserDto userDto);

    void like(Long userTgIdWhoLikes, Long userTgIdWhoIsLike);

    List<UserDto> findFavorites(Long userTgId);

    Page<UserDto> findUsersWithPageable(Long userTgId, int page, int size);

    UserDto update(UserDto userDto);
}