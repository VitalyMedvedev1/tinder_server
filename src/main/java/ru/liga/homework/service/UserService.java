package ru.liga.homework.service;

import org.springframework.data.domain.Page;
import ru.liga.homework.model.UserElement;

import java.util.List;

public interface UserService {
    UserElement findByTgId(Long userTgId);

    UserElement create(UserElement userElement);

    void like(Long userTgIdWhoLikes, Long userTgIdWhoIsLike);

    List<UserElement> findFavorites(Long userTgId);

    Page<UserElement> findUsersWithPageable(Long userTgId, int page, int size);

    UserElement update(UserElement userElement);
}