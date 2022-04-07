package ru.liga.homework.api;

import org.springframework.data.domain.Page;
import ru.liga.homework.model.User.UserView;

import java.util.List;

public interface UserService {
    UserView find(String userName);

    UserView create(UserView userView);

    void like(String userNameWhoLikes, String userNameWhoIsLike);

    List<UserView> findFavorites(String userName);

    Page<UserView> findUsersWithPageable(String userName, int offset, int size);

    UserView update(UserView userView);
}