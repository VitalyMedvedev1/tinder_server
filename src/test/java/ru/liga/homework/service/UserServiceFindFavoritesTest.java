package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.*;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.type.StaticConstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class UserServiceFindFavoritesTest {

    public final UserRepository userRepository = Mockito.mock(UserRepository.class);
    public final UserService userService = new DefaultUserService(userRepository, new ModelMapper(), new UserMapper(new ModelMapper()), new DefaultUsersFormService(new FileWorker()), new ConvertTextToPreRevolution(), new FileWorker());

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

        Mockito.when(userRepository.findByUsername("")).thenReturn(java.util.Optional.of(user));
        List<UserView> listUsers = userService.findFavorites("");

        assertEquals(7, listUsers.size());
        assertEquals(StaticConstant.LOVE, listUsers.stream().filter(userView -> userView.getId() == 7).map(UserView::getDescription).collect(Collectors.joining()));
        assertEquals(StaticConstant.MUTUAL_LOVE, listUsers.stream().filter(userView -> userView.getId() == 1).map(UserView::getDescription).collect(Collectors.joining()));
        assertEquals(StaticConstant.YOU_ARE_LOVE, listUsers.stream().filter(userView -> userView.getId() == 5).map(UserView::getDescription).collect(Collectors.joining()));
    }
}