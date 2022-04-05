package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceLikeFunctionalTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    public final UserService userService = new DefaultUserService(userRepository, new ModelMapper(), new UserMapper(new ModelMapper()), new DefaultUsersFormService(new FileWorker()), new ConvertTextToPreRevolution(), new FileWorker());

    @Test
    void like() {
        User user0 = new User(0, "1", "0", "1", "1", "1", "1", "1", "1", new HashSet<>(), new HashSet<>());
        User user1 = new User(1, "1", "1", "1", "1", "1", "1", "1", "1", new HashSet<>(), new HashSet<>());

        Mockito.when(userRepository.findByUsername("0")).thenReturn(java.util.Optional.of(user0));
        Mockito.when(userRepository.findByUsername("1")).thenReturn(java.util.Optional.of(user1));
        assertEquals(0, user0.getLikes().size());
        assertEquals(0, user0.getLikeBy().size());

        userService.like("0","1");

        assertEquals(1, user0.getLikes().size());
    }
}