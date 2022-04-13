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

@ExtendWith(MockitoExtension.class)
class UserServiceUpdateUserTestImpl {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final FileWorker fileWorker = Mockito.mock(FileWorker.class);
    private final UsersForm usersForm = Mockito.mock(UsersForm.class);
    private ModelMapper modelMapper = new ModelMapper();
    private final UserService userService = new UserServiceImpl(userRepository, usersForm, new ConvertTextToPreRevolution(), fileWorker, UserMapper.USER_MAPPER);
    private static final String CREATE_CLIENT_LOGIN_TEST = "CREATE_CLIENT_LOGIN_TEST";
    private static final String CREATE_CLIENT_PASSWORD_TEST = "CREATE_CLIENT_PASSWORD_TEST";
    private static final Long ID_FUTURE_VERSION = 10103L;
    private static final String TEST_FILE_NAME = "TEST_FILE_NAME";


    @Test
    void updateWith_NOT_CreatedNewForm() {
        UserDto userDto = new UserDto(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.MALE, "1", LoveSearch.ALL, "TEST_FILE_999.txt", "1", "DESCRIPTION", "", new HashSet<>(), new HashSet<>());
        User user = new User(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.FEMALE, "HEADER1", LoveSearch.ALL, "DESCRIPTION", "1", new HashSet<>(), new HashSet<>());
        Mockito.when(userRepository.findByUsertgid(ID_FUTURE_VERSION)).thenReturn(java.util.Optional.of(user));
        User user1 = modelMapper.map(userDto, User.class);
        Mockito.when(userRepository.save(modelMapper.map(userDto, User.class))).thenReturn(user);
        UserDto userDtoNew = userService.update(userDto);

        Mockito.verify(usersForm, Mockito.times(1)).createUserForm(ID_FUTURE_VERSION, "1", "DESCRIPTION");
    }

    @Test
    void updateWithCreatedNewForm() {
        UserDto userDto = new UserDto(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.MALE, "1", LoveSearch.ALL, "TEST_FILE_999.txt", "1", "DESCRIPTION", "", new HashSet<>(), new HashSet<>());
        User user = new User(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.FEMALE, "LoveSearch.ALL", LoveSearch.ALL, "DESCRIPTION", "1", new HashSet<>(), new HashSet<>());
        Mockito.when(userRepository.findByUsertgid(ID_FUTURE_VERSION)).thenReturn(java.util.Optional.of(user));
        User user1 = modelMapper.map(userDto, User.class);
        Mockito.when(userRepository.save(modelMapper.map(userDto, User.class))).thenReturn(user);
        UserDto userDtoNew = userService.update(userDto);

        Mockito.verify(usersForm, Mockito.times(1)).createUserForm(ID_FUTURE_VERSION, "1", "DESCRIPTION");
    }
}