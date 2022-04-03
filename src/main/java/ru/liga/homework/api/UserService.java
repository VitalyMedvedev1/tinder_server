package ru.liga.homework.api;

import ru.liga.homework.model.User.UserView;

import java.util.List;

public interface UserService {
    UserView find(String userName);

    UserView create(UserView userView);

    void like(String userNameWhoLikes, String userNameWhoIsLike);

    void findFavorites(String userName);

    UserView findUsersWithPageable(String userName, int limit, int offset);
}
