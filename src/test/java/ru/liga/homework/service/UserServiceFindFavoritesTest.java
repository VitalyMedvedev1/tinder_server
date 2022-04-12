package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.*;

import ru.liga.homework.repository.entity.User;
import ru.liga.homework.repository.UserRepository;
import ru.liga.homework.service.impl.UserServiceImpl;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.form.UsersFormGenerator;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;
import ru.liga.homework.model.UserElement;
import ru.liga.homework.constant.Values;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class UserServiceFindFavoritesTest {

    public final UserRepository userRepository = Mockito.mock(UserRepository.class);
    public final UserService userService = new UserServiceImpl(userRepository, new ModelMapper(), new UserMapper(new ModelMapper()), new UsersFormGenerator(new FileWorker()), new ConvertTextToPreRevolution(), new FileWorker());

    @Test
    public void findFavorites() {
        User user = new User(0L, 0L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
        user.getLikes().addAll(new HashSet<>
                (Arrays.asList(
                        new User(1L, 1L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "", "1", new HashSet<>(), new HashSet<>()),
                        new User(2L, 2L, "2", "2", Gender.MALE, "2", LoveSearch.ALL, "", "2", new HashSet<>(), new HashSet<>()),
                        new User(3L, 3L, "3", "3", Gender.FEMALE, "3", LoveSearch.ALL, "", "3", new HashSet<>(), new HashSet<>()),
                        new User(4L, 4L, "4", "4", Gender.FEMALE, "4", LoveSearch.ALL, "", "4", new HashSet<>(), new HashSet<>()),
                        new User(5L, 5L, "5", "5", Gender.FEMALE, "5", LoveSearch.ALL, "", "5", new HashSet<>(), new HashSet<>())
                )));
        user.getLikeBy().addAll(new HashSet<>
                (Arrays.asList(
                        new User(1L, 1L, "1", "1", Gender.FEMALE, "1",  LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>()),
                        new User(7L, 7L, "7", "7", Gender.FEMALE, "7", LoveSearch.ALL, "", "7", new HashSet<>(), new HashSet<>()),
                        new User(3L, 3L, "3", "3", Gender.FEMALE, "3", LoveSearch.ALL, "", "3", new HashSet<>(), new HashSet<>()),
                        new User(4L, 4L, "4", "4", Gender.FEMALE, "4", LoveSearch.ALL, "", "4", new HashSet<>(), new HashSet<>()),
                        new User(8L, 8L, "8", "8", Gender.FEMALE, "8", LoveSearch.ALL, "", "8", new HashSet<>(), new HashSet<>())
                )));

        Mockito.when(userRepository.findByUsertgid(null)).thenReturn(java.util.Optional.of(user));
        List<UserElement> listUsers = userService.findFavorites(null);

        assertEquals(7, listUsers.size());
        assertEquals(Values.YOU_ARE_LOVE, listUsers.stream().filter(userView -> userView.getId() == 7).map(UserElement::getLoveSign).collect(Collectors.joining()));
        assertEquals(Values.MUTUAL_LOVE, listUsers.stream().filter(userView -> userView.getId() == 1).map(UserElement::getLoveSign).collect(Collectors.joining()));
        assertEquals(Values.LOVE, listUsers.stream().filter(userView -> userView.getId() == 5).map(UserElement::getLoveSign).collect(Collectors.joining()));
    }
    @Test
    public void findFavorites_LOVE() {
        User user = new User(0L, 0L, "1", "1", Gender.FEMALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
        user.getLikeBy().addAll(new HashSet<>
                (Collections.singletonList(
                        new User(1L, 1L, "1", "1", Gender.FEMALE, "1", LoveSearch.ALL, "", "1", new HashSet<>(), new HashSet<>())
                )));

        Mockito.when(userRepository.findByUsertgid(null)).thenReturn(java.util.Optional.of(user));
        List<UserElement> listUsers = userService.findFavorites(null);

        assertEquals(1, listUsers.size());
        assertEquals(Values.YOU_ARE_LOVE, listUsers.stream().limit(1).map(UserElement::getLoveSign).collect(Collectors.joining()));
    }

    @Test
    public void findFavorites_YOUARELOVE() {
        User user = new User(0L, 0L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
        user.getLikes().addAll(new HashSet<>
                (Collections.singletonList(
                        new User(1L, 1L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "", "1", new HashSet<>(), new HashSet<>())
                )));

        Mockito.when(userRepository.findByUsertgid(null)).thenReturn(java.util.Optional.of(user));
        List<UserElement> listUsers = userService.findFavorites(null);

        assertEquals(1, listUsers.size());
        assertEquals(Values.LOVE, listUsers.stream().limit(1).map(UserElement::getLoveSign).collect(Collectors.joining()));
    }

    @Test
    public void findFavorites_MUTUALLOVE() {
        User user = new User(0L, 0L, "1", "1", Gender.FEMALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
        user.getLikes().addAll(new HashSet<>
                (Collections.singletonList(
                        new User(1L, 1L, "1", "1", Gender.FEMALE, "1", LoveSearch.ALL, "", "1", new HashSet<>(), new HashSet<>())
                )));
        user.getLikeBy().addAll(new HashSet<>
                (Collections.singletonList(
                        new User(1L, 1L, "1", "1", Gender.FEMALE, "1", LoveSearch.ALL, "", "1", new HashSet<>(), new HashSet<>())
                )));

        Mockito.when(userRepository.findByUsertgid(null)).thenReturn(java.util.Optional.of(user));
        List<UserElement> listUsers = userService.findFavorites(null);

        assertEquals(1, listUsers.size());
        assertEquals(Values.MUTUAL_LOVE, listUsers.stream().limit(1).map(UserElement::getLoveSign).collect(Collectors.joining()));
    }
}