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
import ru.liga.homework.mapper.UserMapper;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.type.StaticConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final UsersFormService usersFormService;

    @Override
    public UserView find(String userName) {
        log.info("Find user with name: {}", userName);
        UserView userView = userMapper.map(findUserByName(userName));
        String fileName = userView.getFormFileName();
        if (fileName == null) {
            fileName = createFormAndSave(userView);
        }
        createBase64CodeFromUserForm(userView, fileName);
        return userView;
    }

    @Override
    public UserView create(UserView userView) {

        String fileName = createUserForm(userView);
        userView.setFormFileName(fileName);

        log.info("Save user with name: {}", userView.getName());
        User user = userRepository.save(modelMapper.map(userView, User.class));

        userView.setId(user.getId());
        createBase64CodeFromUserForm(userView, userView.getFormFileName());

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
    public List<UserView> findFavorites(String userName) {
        UserView userView = userMapper.map(findUserByName(userName));
        Set<UserView> usersWhoLikes = userView.getLikes();
        Set<UserView> usersWhoIsLike = userView.getLikeBy();

        List<UserView> listFavoriteUsers = usersWhoIsLike.stream()
                .filter(userView1 -> {
                    if (usersWhoLikes.contains(userView1)) {
                        return false;
                    } else {
                        userView1.setDescription(StaticConstant.LOVE);
                        return true;
                    }
                }).collect(Collectors.toList());
        listFavoriteUsers.addAll(usersWhoLikes.stream()
                .peek(userView1 -> {
                    if (usersWhoIsLike.contains(userView1)) {
                        userView1.setDescription(StaticConstant.MUTUAL_LOVE);
                    } else {
                        userView1.setDescription(StaticConstant.YOU_ARE_LOVE);
                    }
                }).collect(Collectors.toList()));
        return listFavoriteUsers;

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
    public UserView findUsersWithPageable(String userName, int offset, int size) {
        User user = findUserByName(userName);
        PageRequest pageable = PageRequest.of(offset, size);
        List<String> genders = new ArrayList<>();
        List<String> lookingFor = new ArrayList<>();
        addSearchCriteria(user, genders, lookingFor);

        return userRepository.findUsers(user.getId(), genders, lookingFor, pageable).stream()
                .map(user1 -> modelMapper.map(user1, UserView.class))
                .peek(userView1 -> {
                    String fileName = userView1.getFormFileName();
                    if (fileName == null) {
                        createBase64CodeFromUserForm(userView1, createFormAndSave(userView1));
                    } else {
                        createBase64CodeFromUserForm(userView1, fileName);
                    }
                })
                .findFirst().orElseThrow(
                        () -> {
                            log.error("Error when find next user for like");
                            throw new BusinessLogicException("Больше нет анкет подходящих вам");
                        });
    }

    private String createFormAndSave(UserView userView) {
        String fileName;
        log.info("Create user form when find user but not find his form, username: {}", userView.getUsername());
        fileName = createUserForm(userView);
        userView.setFormFileName(fileName);
        userRepository.save(modelMapper.map(userView, User.class));
        return fileName;
    }

    private String createUserForm(UserView userView) {
        log.info("Create form for user with name: {} id: {}", userView.getName(), userView.getId());
        String fileName = usersFormService.createUserForm(userView.getUsername(), userView.getHeader(), userView.getDescription());

        log.info("Save form on user with formName: {} username: {}", fileName, userView.getUsername());
        return fileName;
    }

    private void createBase64CodeFromUserForm(UserView userView, String fileName) {
        log.info("Code attach in Base64");
        String formBase64 = usersFormService.getUserFormInBase64Format(fileName);
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