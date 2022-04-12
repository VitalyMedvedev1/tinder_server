package ru.liga.homework.service.impl;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.util.form.UsersForm;
import ru.liga.homework.repository.entity.User;
import ru.liga.homework.repository.UserRepository;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.UserElement;
import ru.liga.homework.service.UserService;
import ru.liga.homework.constant.Values;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final UsersForm usersForm;
    private final ConvertTextToPreRevolution convertTextToPreRevolution;
    private final FileWorker fileWorker;

    @Override
    public UserElement findByTgId(Long userTgId) {
        log.info("Find user with Telegram Id: {}", userTgId);

        UserElement userElement = userMapper.map(findUserByTgId(userTgId));
        String fileName = userElement.getFormFileName();
        if (StringUtils.isEmpty(fileName)) {
            fileName = createFormAndSave(userElement);
        }
        createBase64CodeFromUserForm(userElement, fileName);
        return userElement;
    }

    @Override
    public UserElement create(UserElement userElement) {

        log.info("Find user with Telegram Id: {}", userElement.getUsertgid());
        if (userRepository.findByUsertgid(userElement.getUsertgid()).isPresent()) {
            throw new BusinessLogicException(MessageFormat.format("User with Telegram Id: {0} already exist", userElement.getUsertgid()));
        }

        log.info("Save user with Telegram Id: {}", userElement.getUsertgid());
        User user = userRepository.save(modelMapper.map(userElement, User.class));
        String fileName;
        try {
            fileName = createUserForm(userElement);
            userElement.setFormFileName(fileName);
            userElement.setId(user.getId());
            userRepository.save(modelMapper.map(userElement, User.class));
        } catch (Exception e) {
            log.error("Error wen create user form with Telegram Id {} \n Error message: {}", userElement.getUsertgid(), e.getMessage());
            if ((fileName = userElement.getFormFileName()) != null) {
                fileWorker.deleteFileFromDisc(fileName);
            }
            throw new BusinessLogicException("Error create new user with Telegram Id: " + e.getMessage());
        }

        createBase64CodeFromUserForm(userElement, fileName);
        return userElement;
    }


    @Override
    public void like(Long userTgIdWhoLikes, Long userTgIdWhoIsLike) {
        if (userTgIdWhoLikes.equals(userTgIdWhoIsLike)) {
            throw new BusinessLogicException("User cant like yourself, Telegram Id: " + userTgIdWhoLikes);
        }
        User userWhoLikes = findUserByTgId(userTgIdWhoLikes);
        User userWhoIsLike = findUserByTgId(userTgIdWhoIsLike);

        log.info("Save new record - like, user who like Telegram Id: {}, user who was like Telegram Id: {}", userWhoLikes.getUsertgid(), userWhoIsLike.getUsertgid());
        userWhoLikes.getLikes().add(userWhoIsLike);
    }

    @Override
    public List<UserElement> findFavorites(Long userTgId) {
        User user = findUserByTgId(userTgId);
        Set<User> usersWhoLikes = user.getLikes();
        Set<User> usersWhoIsLike = user.getLikeBy();

        List<UserElement> userElementList = usersWhoIsLike.stream()
                .filter(user1 -> !usersWhoLikes.contains(user1))
                .map(user1 -> {
                    UserElement userElement = userMapper.map(user1);
                    userElement.setLoveSign(Values.YOU_ARE_LOVE);
                    return userElement;
                }).collect(Collectors.toList());
        userElementList.addAll(
                usersWhoLikes.stream()
                        .map(user1 -> {
                            UserElement userElement = userMapper.map(user1);
                            if (usersWhoIsLike.contains(user1)) {
                                userElement.setLoveSign(Values.MUTUAL_LOVE);
                            } else {
                                userElement.setLoveSign(Values.LOVE);
                            }
                            return userElement;
                        }).collect(Collectors.toList())
        );
        return userElementList;
    }

    @Override
    public Page<UserElement> findUsersWithPageable(Long userTgId, int page, int size) {
        User user = findUserByTgId(userTgId);
        PageRequest pageable = PageRequest.of(page, size);
        List<String> genders = new ArrayList<>();
        List<String> lookingFor = new ArrayList<>();
//        addSearchCriteria(user, genders, lookingFor);

        Page<User> userPage = userRepository.findUsers(user.getId(), genders, lookingFor, pageable);
        if (userPage == null) {
            throw new BusinessLogicException(MessageFormat.format("Form for user Telegram Id: {0} not found", userTgId));
        }

        List<UserElement> listUserElement = userPage.getContent()
                .stream()
                .map(user1 -> modelMapper.map(user1, UserElement.class)).peek(userView1 -> {
                    String fileName = userView1.getFormFileName();
                    if (fileName == null) {
                        createBase64CodeFromUserForm(userView1, createFormAndSave(userView1));
                    } else {
                        createBase64CodeFromUserForm(userView1, fileName);
                    }
                }).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(listUserElement, pageable, userPage::getTotalElements);
    }

    @Override
    public UserElement update(UserElement userElement) {
        log.info("Update user, Telegram Id: {}", userElement.getUsertgid());
        User user = findUserByTgId(userElement.getUsertgid());
        String fileName = user.getFormFileName();
        if (!userElement.getFormTitle().equals(user.getFormTitle()) || !userElement.getFormDescription().equals(user.getFormDescription())) {
            fileWorker.deleteFileFromDisc(fileName);
            try {
                fileName = createUserForm(userElement);
                userElement.setFormFileName(fileName);
                userElement.setId(user.getId());
            } catch (Exception e) {
                log.error("Error wen create user form with Telegram Id {} \n Error message: {}", userElement.getUsertgid(), e.getMessage());
                if ((fileName = userElement.getFormFileName()) != null) {
                    fileWorker.deleteFileFromDisc(fileName);
                }
                throw new BusinessLogicException("Error create new user: " + e.getMessage());
            }
        }
        userElement.setId(user.getId());
        userElement.setFormFileName(fileName);
        User userNewData = modelMapper.map(userElement, User.class);
        userNewData.getLikes().addAll(user.getLikes());
        userNewData.getLikeBy().addAll(user.getLikeBy());
        userRepository.save(userNewData);
        createBase64CodeFromUserForm(userElement, fileName);
        return userElement;
    }

    private User findUserByTgId(Long userTgId) {
        log.info("Find user with UserName: {}", userTgId);
        return userRepository.findByUsertgid(userTgId).
                orElseThrow(
                        () -> new EntityNotFoundException("User not found with Telegram Id: " + userTgId));
    }

    private String createFormAndSave(UserElement userElement) {
        String fileName;
        log.info("Create user form when find user but not find his form, username: {}", userElement.getUsertgid());
        fileName = createUserForm(userElement);
        userElement.setFormFileName(fileName);
        userRepository.save(modelMapper.map(userElement, User.class));
        return fileName;
    }

    private String createUserForm(UserElement userElement) {
        log.info("Create form for user with UserName: {} id: {}", userElement.getUsertgid(), userElement.getId());
        String preRevolutionFormTitle = convertTextToPreRevolution.convert(userElement.getFormTitle());
        String preRevolutionFormDesc = convertTextToPreRevolution.convert(userElement.getFormDescription());
        String fileName = usersForm.createUserForm(userElement.getUsertgid(),
                preRevolutionFormTitle,
                preRevolutionFormDesc);

        log.info("Save form on user with formName: {} username: {}", fileName, userElement.getUsertgid());
        return fileName;
    }

    private void createBase64CodeFromUserForm(UserElement userElement, String fileName) {
        log.info("Code attach in Base64");
        String formBase64 = fileWorker.getUserFormInBase64Format(fileName);
        log.info("Save attach in base64 UserName:" + userElement.getUsertgid());
        userElement.setAttachBase64Code(formBase64);
    }

//    private void addSearchCriteria(User user, List<String> genders, List<String> lookingFor) {
//        switch (user.getLookingFor()) {
//            case "MALES":
//                genders.add("MALE");
//                break;
//            case "FEMALES":
//                genders.add("FEMALE");
//                break;
//            default:
//                genders.add("MALE");
//                genders.add("FEMALE");
//                break;
//        }
//        if ("MALE".equals(user.getGender())) {
//            lookingFor.add("MALE");
//        } else {
//            lookingFor.add("FEMALE");
//        }
//        lookingFor.add("ALL");
//    }
}