package ru.liga.homework.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.model.User.UserView;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserView findUserByName(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("нету"));
        return modelMapper.map(user, UserView.class);
    }

    @Override
    public void createUser(UserView userView) {
        userRepository.save(modelMapper.map(userView, User.class));
    }
}
