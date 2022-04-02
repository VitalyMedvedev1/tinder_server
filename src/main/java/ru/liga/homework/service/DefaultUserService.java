package ru.liga.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.api.UserService;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.User.UserView;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UsersFormService usersFormService;

    @Override
    @Transactional
    public UserView findUser(String userName) {
        User user = findUserByName(userName);
        return modelMapper.map(findUserByName(userName), UserView.class);
    }

    @Override
    @Transactional
    public UserView create(UserView userView) {
        log.info("Save user with name: {}", userView.getName());
        User user = userRepository.save(modelMapper.map(userView, User.class));
        userView.setId(user.getId());

        log.info("Create form for user with name: {} id: {}", userView.getName(), user.getId());
        String fileName = usersFormService.createUserForm(user.getId(), user.getHeader(), user.getDescription());

        log.info("Save form for user with formName: {} userId: {}", userView.getName(), user.getId());
        usersFormService.saveFileNameInDb(fileName, user.getId());

        log.info("Code attach in Base64");
        String formBase64 = usersFormService.getUserFormInBase64Format(user.getId());

        log.info("Return UserView");
        userView.setAttachBase64Code(formBase64);

        return userView;
    }

    @Override
    @Transactional
    public void like(String userNameWhoLikes, String userNameWhoIsLike) {
        if (userNameWhoLikes.equals(userNameWhoIsLike)) {
            throw new BusinessLogicException("User cant like yourself, name: " + userNameWhoLikes);
        }
        User userWhoLikes = findUserByName(userNameWhoLikes);
        User userWhoIsLike = findUserByName(userNameWhoIsLike);

        log.info("Save new record - like, user who like: {}, user who was like: {}", userWhoLikes, userWhoIsLike);
        userWhoLikes.getLikes().add(userWhoIsLike);
    }

    @Override
    @Transactional
    public void findFavorites(String userName) {
        User user = findUserByName(userName);
        Set<User> usersWhoLikes = user.getLikes();
        Set<User> usersWhoIsLike = user.getLikeBy();
        List<User> users = new ArrayList<>(user.getLikes());
        users.addAll(user.getLikeBy());
        System.out.println(123);
    }

    private User findUserByName(String userName) {
        log.info("Find user with name: {}", userName);
        return userRepository.findByUsername(userName).orElseThrow(
                () -> {
                    log.error("User not found with login: {}", userName);
                    return new BusinessLogicException("User not found with login: " + userName);
                });
    }

    @Override
    public List<UserView> findUsersWithPageable(String userName, int limit, int offset) {
        User user = findUserByName(userName);
        PageRequest pageable = PageRequest.of(limit, offset);
/*
        String lookingFor = user.getLookingFor().equals("MALES") ? "MALE" :
                user.getLookingFor().equals("FEMALES") ? "FEMALE" : "IS NOT NULL";*/

        return userRepository.findUsers(userName, pageable).stream().map(user1 -> modelMapper.map(user1, UserView.class)).collect(Collectors.toList());
    }
}