package ru.liga.homework.api;

import org.springframework.data.domain.Page;
import ru.liga.homework.model.User.UserView;

import java.util.List;

public interface UserService {
    UserView find(Long userTgId);

    UserView create(UserView userView);

    void like(Long userTgIdWhoLikes, Long userTgIdWhoIsLike);

    List<UserView> findFavorites(Long userTgId);

    Page<UserView> findUsersWithPageable(Long userTgId, int offset, int size);

    UserView update(UserView userView);
}