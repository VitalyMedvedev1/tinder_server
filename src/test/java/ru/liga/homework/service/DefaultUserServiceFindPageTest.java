package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceFindPageTest {

    public final UserRepository userRepository = Mockito.mock(UserRepository.class);
    public final FileWorker fileWorker = Mockito.mock(FileWorker.class);
    public final UserService userService = new DefaultUserService(userRepository, new ModelMapper(), new UserMapper(new ModelMapper()), new DefaultUsersFormService(fileWorker), new ConvertTextToPreRevolution(), fileWorker);

    @Test
    void findUsersWithPageable() {
        User user = new User(0L, 0L, "1", "1", "1", "1", "1", "1", "1", new HashSet<>(), new HashSet<>());
        List<User> list = new ArrayList<>(Arrays.asList(
                new User(1L, 1L, "1", "1", "1", "1", "1", "", "1", new HashSet<>(), new HashSet<>()),
                new User(2L, 2L, "2", "2", "2", "2", "2", "", "1", new HashSet<>(), new HashSet<>()),
                new User(1L, 1L, "1", "1", "1", "1", "1", "", "1", new HashSet<>(), new HashSet<>())));

        PageRequest pageable = PageRequest.of(1, 1);
        Page<User> userPage = new PageImpl<>(list, pageable, list.size());
        Mockito.when(userRepository.findByUsertgid(0L)).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.findUsers(user.getId(), new ArrayList<>(Arrays.asList("MALE", "FEMALE")), new ArrayList<>(Arrays.asList( "FEMALE", "ALL")), pageable)).thenReturn(userPage);
        Mockito.when(fileWorker.getUserFormInBase64Format("1")).thenReturn("3333");
        Page<UserView> userViewPage = userService.findUsersWithPageable(0L, 1, 1);
        assertEquals(1, userPage.getSize());
    }

}