package ru.liga.homework.api;

import ru.liga.homework.model.User.UserView;

public interface UserService {
    UserView findUserByName(String userName);

    void create(UserView userView);

    void like(String userWhoLikes, String userWhoIsLike);

    void findFavorites(String userName);
}
