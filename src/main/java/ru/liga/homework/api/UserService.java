package ru.liga.homework.api;

import ru.liga.homework.model.User.UserView;

public interface UserService {
    UserView findUser(String userName);

    void create(UserView userView);

    void like(String userNameWhoLikes, String userNameWhoIsLike);

    void findFavorites(String userName);
}
