package ru.liga.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.api.UserService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.User.UserView;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public UserView findUserByName(String userName) {
        log.info("Find user with name: {}", userName);
        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> {
                    log.error("User not found with login: {}" + userName);
                    return new BusinessLogicException("User not found with login: " + userName);
                });
        return modelMapper.map(user, UserView.class);
    }

    @Override
    @Transactional
    public void create(UserView userView) {
        log.info("Save user with name: {}", userView.getUsername());
        userRepository.save(modelMapper.map(userView, User.class));
    }

    @Override
    @Transactional
    public void like(String userWhoLikes, String userWhoIsLike) {
        User user1 = userRepository.findByUsername(userWhoLikes).get();
        User user2 = userRepository.findByUsername(userWhoIsLike).get();

        //user1.getLikes().add(user2);

        log.info("Save new record - like, user who like: {}, user who was like: {}", userWhoLikes, userWhoIsLike);

    }

    @Override
    @Transactional
    public void findFavorites(String userName) {

    }
}