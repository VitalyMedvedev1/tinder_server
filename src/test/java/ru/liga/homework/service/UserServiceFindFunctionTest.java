package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.liga.homework.model.UserDto;
import ru.liga.homework.model.mapper.UserMapper;
import ru.liga.homework.repository.UserRepository;
import ru.liga.homework.repository.entity.User;
import ru.liga.homework.service.impl.UserServiceImpl;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.form.UsersForm;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceFindFunctionTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UsersForm usersForm = Mockito.mock(UsersForm.class);
    private final FileWorker fileWorker = Mockito.mock(FileWorker.class);
    private ModelMapper modelMapper = new ModelMapper();
    private final UserService userService = new UserServiceImpl(userRepository, usersForm, new ConvertTextToPreRevolution(), fileWorker, UserMapper.USER_MAPPER);
    private static final String CREATE_CLIENT_LOGIN_TEST = "CREATE_CLIENT_LOGIN_TEST";
    private static final String CREATE_CLIENT_PASSWORD_TEST = "CREATE_CLIENT_PASSWORD_TEST";
    private static final Long ID_FUTURE_VERSION = 10103L;
    private static final String TEST_FILE_NAME = "TEST_FILE_NAME";

    @Test
    void find() {
        User user0 = new User(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.MALE, "1", LoveSearch.ALL, "1", "123.txt", new HashSet<>(), new HashSet<>());

        Mockito.when(fileWorker.getUserFormInBase64Format(null)).thenReturn("3333");
        Mockito.when(userRepository.findByUsertgid(ID_FUTURE_VERSION)).thenReturn(java.util.Optional.of(user0));
        UserDto userDto = userService.findByTgId(ID_FUTURE_VERSION);

        assertEquals(userDto.getId(), user0.getId());
        assertEquals(userDto.getUsertgid(), user0.getUsertgid());
        assertEquals(userDto.getName(), user0.getName());
        assertEquals(userDto.getPassword(), user0.getPassword());
    }
}