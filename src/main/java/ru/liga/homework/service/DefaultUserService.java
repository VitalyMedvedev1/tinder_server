package ru.liga.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.api.UserService;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.User.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UsersFormService usersFormService;

    @Override
    public UserView find(String userName) {
        UserView userView = modelMapper.map(findUserByName(userName), UserView.class);
        if (userView.getAttach() == null) {
            createUserForm(userView);
        }
        createBase64CodeFromUserForm(userView);
        return userView;
    }

    @Override
    public UserView create(UserView userView) {
        log.info("Save user with name: {}", userView.getName());
        User user = userRepository.save(modelMapper.map(userView, User.class));
        userView.setId(user.getId());

        createUserForm(userView);
        createBase64CodeFromUserForm(userView);

        return userView;
    }

    @Override
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
    public UserView findUsersWithPageable(String userName, int limit, int offset) {
        User user = findUserByName(userName);
        PageRequest pageable = PageRequest.of(limit, offset);
        List<String> genders = new ArrayList<>();
        List<String> lookingFor = new ArrayList<>();
        addSearchCriteria(user, genders, lookingFor);

        return userRepository.findUsers(user.getId(), genders, lookingFor, pageable).stream()
                .map(user1 -> modelMapper.map(user1, UserView.class))
                .peek(userView1 -> userView1.setAttachBase64Code(usersFormService.getUserFormInBase64Format(userView1.getId())))
                .findFirst().orElseThrow(
                        () -> {
                            log.error("Error when find next user for like");
                            throw new BusinessLogicException("Больше нет анкет подходящих вам");
                        });
    }

    private void createUserForm(UserView userView) {
        log.info("Create form for user with name: {} id: {}", userView.getName(), userView.getId());
        String fileName = usersFormService.createUserForm(userView.getId(), userView.getHeader(), userView.getDescription());

        log.info("Save form for user with formName: {} userId: {}", userView.getName(), userView.getId());
        usersFormService.saveFileNameInDb(fileName, userView.getId());
    }

    private void createBase64CodeFromUserForm(UserView userView) {
        log.info("Code attach in Base64");
        String formBase64 = usersFormService.getUserFormInBase64Format(userView.getId());

        log.info("Save attach in base64 UserView");
        userView.setAttachBase64Code(formBase64);
    }

    public void addSearchCriteria(User user, List<String> genders, List<String> lookingFor) {
        switch (user.getLookingFor()) {
            case "MALES":
                genders.add("MALE");
                break;
            case "FEMALES":
                genders.add("FEMALE");
            default:
                genders.add("MALE");
                genders.add("FEMALE");
                break;
        }
        if ("MALE".equals(user.getGender())) {
            lookingFor.add("MALE");
        } else {
            lookingFor.add("FEMALE");
        }
        lookingFor.add("ALL");
    }
}