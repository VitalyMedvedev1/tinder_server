package ru.liga.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.liga.homework.api.UserService;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceFindFunctionTest {

    public final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UsersFormService usersFormService = Mockito.mock(UsersFormService.class);
    public final FileWorker fileWorker = Mockito.mock(FileWorker.class);
    public ModelMapper modelMapper = new ModelMapper();
    public final UserService userService = new DefaultUserService(userRepository, modelMapper, new UserMapper(new ModelMapper()), usersFormService, new ConvertTextToPreRevolution(), fileWorker);
    private static final String CREATE_CLIENT_LOGIN_TEST = "CREATE_CLIENT_LOGIN_TEST";
    private static final String CREATE_CLIENT_PASSWORD_TEST = "CREATE_CLIENT_PASSWORD_TEST";
    private static final Long ID_FUTURE_VERSION = 10103L;
    private static final String TEST_FILE_NAME = "TEST_FILE_NAME";

    @Test
    void find() {
        User user0 = new User(ID_FUTURE_VERSION, ID_FUTURE_VERSION, CREATE_CLIENT_LOGIN_TEST, CREATE_CLIENT_PASSWORD_TEST, "1", "1", "1", "1", "123.txt", new HashSet<>(), new HashSet<>());

        Mockito.when(fileWorker.getUserFormInBase64Format(null)).thenReturn("3333");
        Mockito.when(userRepository.findByUsertgid(ID_FUTURE_VERSION)).thenReturn(java.util.Optional.of(user0));
        UserView userView = userService.find(ID_FUTURE_VERSION);

        assertEquals(userView.getId(), user0.getId());
        assertEquals(userView.getUsertgid(), user0.getUsertgid());
        assertEquals(userView.getName(), user0.getName());
        assertEquals(userView.getPassword(), user0.getPassword());
    }
}