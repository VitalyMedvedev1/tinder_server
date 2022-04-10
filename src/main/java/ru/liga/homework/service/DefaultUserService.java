package ru.liga.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.api.UserService;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.db.repository.UserRepository;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.User.UserView;
import ru.liga.homework.type.StaticConstant;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
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
    private final ConvertTextToPreRevolution convertTextToPreRevolution;
    private final FileWorker fileWorker;

    @Override
    public UserView find(Long userTgId) {
        log.info("Find user with UserName: {}", userTgId);

        UserView userView = userMapper.map(findUserByTgId(userTgId));
        String fileName = userView.getFormFileName();
        if (fileName == null || fileName.equals("")) {
            fileName = createFormAndSave(userView);
        }
        createBase64CodeFromUserForm(userView, fileName);
        return userView;
    }

    @Override
    public UserView create(UserView userView) {

        log.info("Find user with UserName: {}", userView.getUsertgid());
        if (userRepository.findByUsertgid(userView.getUsertgid()).isPresent()) {
            throw new BusinessLogicException("User with name already exist: " + userView.getUsertgid());
        }

        log.info("Save user with UserName: {}", userView.getUsertgid());
        User user = userRepository.save(modelMapper.map(userView, User.class));
        String fileName;
        try {
            fileName = createUserForm(userView);
            userView.setFormFileName(fileName);
            userView.setId(user.getId());
            userRepository.save(modelMapper.map(userView, User.class));
        } catch (Exception e) {
            log.error("Error wen create user form with login {} \n Error message: {}", userView.getUsertgid(), e.getMessage());
            if ((fileName = userView.getFormFileName()) != null) {
                fileWorker.deleteFileFromDisc(fileName);
            }
            throw new BusinessLogicException("Error create new user: " + e.getMessage());
        }

        createBase64CodeFromUserForm(userView, fileName);
        return userView;
    }


    @Override
    public void like(Long userTgIdWhoLikes, Long userTgIdWhoIsLike) {
        if (userTgIdWhoLikes.equals(userTgIdWhoIsLike)) {
            throw new BusinessLogicException("User cant like yourself, name: " + userTgIdWhoLikes);
        }
        User userWhoLikes = findUserByTgId(userTgIdWhoLikes);
        User userWhoIsLike = findUserByTgId(userTgIdWhoIsLike);

        log.info("Save new record - like, user who like: {}, user who was like: {}", userWhoLikes, userWhoIsLike);
        userWhoLikes.getLikes().add(userWhoIsLike);
    }

    @Override
    public List<UserView> findFavorites(Long userTgId) {
        User user = findUserByTgId(userTgId);
        Set<User> usersWhoLikes = user.getLikes();
        Set<User> usersWhoIsLike = user.getLikeBy();

        List<UserView> userViewList = usersWhoIsLike.stream()
                .filter(user1 -> !usersWhoLikes.contains(user1))
                .map(user1 -> {
                    UserView userView = userMapper.map(user1);
                    userView.setLoveSign(StaticConstant.YOU_ARE_LOVE);
                    return userView;
                }).collect(Collectors.toList());
        userViewList.addAll(
                usersWhoLikes.stream()
                        .map(user1 -> {
                            UserView userView = userMapper.map(user1);
                            if (usersWhoIsLike.contains(user1)) {
                                userView.setLoveSign(StaticConstant.MUTUAL_LOVE);
                            } else {
                                userView.setLoveSign(StaticConstant.LOVE);
                            }
                            return userView;
                        }).collect(Collectors.toList())
        );
        return userViewList;
    }

    @Override
    public Page<UserView> findUsersWithPageable(Long userTgId, int page, int size) {
        User user = findUserByTgId(userTgId);
        PageRequest pageable = PageRequest.of(page, size);
        List<String> genders = new ArrayList<>();
        List<String> lookingFor = new ArrayList<>();
        addSearchCriteria(user, genders, lookingFor);

        Page<User> userPage = userRepository.findUsers(user.getId(), genders, lookingFor, pageable);
        if (userPage == null) {
            throw new BusinessLogicException(MessageFormat.format("Form for user: {0} not found", userTgId));
        }

        List<UserView> listUserView = userPage.getContent()
                .stream()
                .map(user1 -> modelMapper.map(user1, UserView.class)).peek(userView1 -> {
                    String fileName = userView1.getFormFileName();
                    if (fileName == null) {
                        createBase64CodeFromUserForm(userView1, createFormAndSave(userView1));
                    } else {
                        createBase64CodeFromUserForm(userView1, fileName);
                    }
                }).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(listUserView, pageable, userPage::getTotalElements);
    }

    @Override
    public UserView update(UserView userView) {
        log.info("Update user, UserName: {}", userView.getUsertgid());
        User user = findUserByTgId(userView.getUsertgid());
        String fileName = user.getFormFileName();
        if (!userView.getHeader().equals(user.getHeader()) || !userView.getDescription().equals(user.getDescription())) {
            fileWorker.deleteFileFromDisc(fileName);
            try {
                fileName = createUserForm(userView);
                userView.setFormFileName(fileName);
                userView.setId(user.getId());
            } catch (Exception e) {
                log.error("Error wen create user form with login {} \n Error message: {}", userView.getUsertgid(), e.getMessage());
                if ((fileName = userView.getFormFileName()) != null) {
                    fileWorker.deleteFileFromDisc(fileName);
                }
                throw new BusinessLogicException("Error create new user: " + e.getMessage());
            }
        }
        userView.setId(user.getId());
        userView.setFormFileName(fileName);
        User userNewData = modelMapper.map(userView, User.class);
        userNewData.getLikes().addAll(user.getLikes());
        userNewData.getLikeBy().addAll(user.getLikeBy());
        userRepository.save(userNewData);
        createBase64CodeFromUserForm(userView, fileName);
        return userView;
    }

    private User findUserByTgId(Long userTgId) {
        log.info("Find user with UserName: {}", userTgId);
        return userRepository.findByUsertgid(userTgId).orElseThrow(
                () -> {
                    log.error("User not found with login: {}", userTgId);
                    return new EntityNotFoundException("User not found with login: " + userTgId);
                });
    }

    private String createFormAndSave(UserView userView) {
        String fileName;
        log.info("Create user form when find user but not find his form, username: {}", userView.getUsertgid());
        fileName = createUserForm(userView);
        userView.setFormFileName(fileName);
        userRepository.save(modelMapper.map(userView, User.class));
        return fileName;
    }

    private String createUserForm(UserView userView) {
        log.info("Create form for user with UserName: {} id: {}", userView.getUsertgid(), userView.getId());
        String preRevolutionHeader = convertTextToPreRevolution.convert(userView.getHeader());
        String preRevolutionDesc = convertTextToPreRevolution.convert(userView.getDescription());
        String fileName = usersFormService.createUserForm(userView.getUsertgid(),
                preRevolutionHeader,
                preRevolutionDesc);

        log.info("Save form on user with formName: {} username: {}", fileName, userView.getUsertgid());
        return fileName;
    }

    private void createBase64CodeFromUserForm(UserView userView, String fileName) {
        log.info("Code attach in Base64");
        String formBase64 = fileWorker.getUserFormInBase64Format(fileName);
        log.info("Save attach in base64 UserName:" + userView.getUsertgid());
        userView.setAttachBase64Code(formBase64);
    }

    private void addSearchCriteria(User user, List<String> genders, List<String> lookingFor) {
        switch (user.getLookingFor()) {
            case "MALES":
                genders.add("MALE");
                break;
            case "FEMALES":
                genders.add("FEMALE");
                break;
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