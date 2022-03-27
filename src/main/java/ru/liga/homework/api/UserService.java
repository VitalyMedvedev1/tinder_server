package ru.liga.homework.api;

import ru.liga.homework.model.User.UserView;

public interface UserService {
    UserView findUserByName(String userName);

    void createUser(UserView userView);
}
