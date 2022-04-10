package ru.liga.homework.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.liga.homework.api.UserService;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LookingFor;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceUpdateUserTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final FileWorker fileWorker = Mockito.mock(FileWorker.class);
    private final UsersFormService usersFormService = Mockito.mock(UsersFormService.class);
    private ModelMapper modelMapper = new ModelMapper();
    private final UserService userService = new DefaultUserService(userRepository, modelMapper, new UserMapper(new ModelMapper()), usersFormService, new ConvertTextToPreRevolution(), fileWorker);
    private static final String CREATE_CLIENT_LOGIN_TEST = "CREATE_CLIENT_LOGIN_TEST";
    private static final String CREATE_CLIENT_PASSWORD_TEST = "CREATE_CLIENT_PASSWORD_TEST";
    private static final Long ID_FUTURE_VERSION = 10103L;
    private static final String TEST_FILE_NAME = "TEST_FILE_NAME";


    @Test
    void updateWith_NOT_CreatedNewForm() {
        UserView userView = new UserView(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.MALE, "1", LookingFor.ALL, "TEST_FILE_999.txt", "1", new HashSet<>(), new HashSet<>(), "DESCRIPTION","");
        User user = new User(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, "1", "HEADER1", "1", "DESCRIPTION", "1", new HashSet<>(), new HashSet<>());
        Mockito.when(userRepository.findByUsertgid(ID_FUTURE_VERSION)).thenReturn(java.util.Optional.of(user));
        User user1 = modelMapper.map(userView, User.class);
        Mockito.when(userRepository.save(modelMapper.map(userView, User.class))).thenReturn(user);
        UserView userViewNew = userService.update(userView);

        Mockito.verify(usersFormService, Mockito.times(1)).createUserForm(ID_FUTURE_VERSION, "1", "Description");
    }

    @Test
    void updateWithCreatedNewForm() {
        UserView userView = new UserView(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, Gender.MALE, "1", LookingFor.ALL, "TEST_FILE_999.txt", "1", new HashSet<>(), new HashSet<>(), "DESCRIPTION","");
        User user = new User(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, "1", "HEADER", "1", "DESCRIPTION", "1", new HashSet<>(), new HashSet<>());
        Mockito.when(userRepository.findByUsertgid(ID_FUTURE_VERSION)).thenReturn(java.util.Optional.of(user));
        User user1 = modelMapper.map(userView, User.class);
        Mockito.when(userRepository.save(modelMapper.map(userView, User.class))).thenReturn(user);
        UserView userViewNew = userService.update(userView);

        Mockito.verify(usersFormService, Mockito.times(1)).createUserForm(ID_FUTURE_VERSION, "1", "Description");
    }
}