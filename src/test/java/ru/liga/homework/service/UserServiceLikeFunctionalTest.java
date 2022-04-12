package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.liga.homework.repository.entity.User;
import ru.liga.homework.repository.UserRepository;
import ru.liga.homework.service.impl.UserServiceImpl;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.form.UsersFormGenerator;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceLikeFunctionalTest {

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    public final UserService userService = new UserServiceImpl(userRepository, new ModelMapper(), new UserMapper(new ModelMapper()), new UsersFormGenerator(new FileWorker()), new ConvertTextToPreRevolution(), new FileWorker());

    @Test
    void like() {
        User user0 = new User(0L, 111L, "0", "1", "1", "1", "1", "1", "1", new HashSet<>(), new HashSet<>());
        User user1 = new User(1L, 222L, "1", "1", "1", "1", "1", "1", "1", new HashSet<>(), new HashSet<>());

        Mockito.when(userRepository.findByUsertgid(111L)).thenReturn(java.util.Optional.of(user0));
        Mockito.when(userRepository.findByUsertgid(222L)).thenReturn(java.util.Optional.of(user1));
        assertEquals(0, user0.getLikes().size());
        assertEquals(0, user0.getLikeBy().size());

        userService.like(111L,222L);

        assertEquals(1, user0.getLikes().size());
    }
}