package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.mapper.UserMapper;

import java.util.Arrays;
import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
class UserServiceFindFavoritesTest {

    public final UserRepository userRepository = Mockito.mock(UserRepository.class);
    public final UserService userService = new DefaultUserService(userRepository, new ModelMapper(), new UserMapper(new ModelMapper()), new DefaultUsersFormService());

    @Test
    public void findFavorites() {
        User user = new User(0, "1", "1", "1", "1", "1", "1", "1", "1", new HashSet<>(), new HashSet<>());
        user.getLikes().addAll(new HashSet<>
                (Arrays.asList(
                        new User(1, "1", "1", "1", "1", "1", "1", "", "1", new HashSet<>(), new HashSet<>()),
                        new User(2, "2", "2", "2", "2", "2", "2", "", "2", new HashSet<>(), new HashSet<>()),
                        new User(3, "3", "3", "3", "3", "3", "3", "", "3", new HashSet<>(), new HashSet<>()),
                        new User(4, "4", "4", "4", "4", "4", "4", "", "4", new HashSet<>(), new HashSet<>()),
                        new User(5, "5", "5", "5", "5", "5", "5", "", "5", new HashSet<>(), new HashSet<>())
                )));
        user.getLikeBy().addAll(new HashSet<>
                (Arrays.asList(
                        new User(1, "1", "1", "1", "1", "1", "1", "", "1", new HashSet<>(), new HashSet<>()),
                        new User(7, "7", "7", "7", "7", "7", "7", "", "7", new HashSet<>(), new HashSet<>()),
                        new User(3, "3", "3", "3", "3", "3", "3", "", "3", new HashSet<>(), new HashSet<>()),
                        new User(4, "4", "4", "4", "4", "4", "4", "", "4", new HashSet<>(), new HashSet<>()),
                        new User(8, "8", "8", "8", "8", "8", "8", "", "8", new HashSet<>(), new HashSet<>())
                )));

        Mockito.when(userRepository.findByUsername("123")).thenReturn(java.util.Optional.of(user));
        userService.findFavorites("123");
    }
}