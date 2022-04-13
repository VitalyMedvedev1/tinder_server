package ru.liga.homework.service.impl;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.constant.Values;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.UserDto;
import ru.liga.homework.model.mapper.UserMapper;
import ru.liga.homework.repository.UserRepository;
import ru.liga.homework.repository.entity.User;
import ru.liga.homework.service.UserService;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;
import ru.liga.homework.util.ConvertTextToPreRevolution;
import ru.liga.homework.util.FileWorker;
import ru.liga.homework.util.form.UsersForm;

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
    private final UsersForm usersForm;
    private final ConvertTextToPreRevolution convertTextToPreRevolution;
    private final FileWorker fileWorker;
    private final UserMapper userMapper;

    @Override
    public UserDto findByTgId(Long userTgId) {
        log.info("Find user with Telegram Id: {}", userTgId);

        User user = findUserByTgId(userTgId);
        UserDto userDto1 = userMapper.userToUserDto(user);

        UserDto userDto = userMapper.userToUserDto(findUserByTgId(userTgId));
        String fileName = userDto.getFormFileName();
        if (StringUtils.isEmpty(fileName)) {
            fileName = createFormAndSave(userDto);
        }
        createBase64CodeFromUserForm(userDto, fileName);
        return userDto;
    }

    @Override
    public UserDto create(UserDto userDto) {

        log.info("Find user with Telegram Id: {}", userDto.getUsertgid());
        if (userRepository.findByUsertgid(userDto.getUsertgid()).isPresent()) {
            throw new BusinessLogicException(MessageFormat.format("User with Telegram Id: {0} already exist", userDto.getUsertgid()));
        }

        log.info("Save user with Telegram Id: {}", userDto.getUsertgid());
        User user = userRepository.save(userMapper.fromUserDto(userDto));
        String fileName;
        try {
            fileName = createUserForm(userDto);
            userDto.setFormFileName(fileName);
            userDto.setId(user.getId());
            userRepository.save(userMapper.fromUserDto(userDto));
        } catch (Exception e) {
            log.error("Error wen create user form with Telegram Id {} \n Error message: {}", userDto.getUsertgid(), e.getMessage());
            if ((fileName = userDto.getFormFileName()) != null) {
                fileWorker.deleteFileFromDisc(fileName);
            }
            throw new BusinessLogicException("Error create new user with Telegram Id: " + e.getMessage());
        }

        createBase64CodeFromUserForm(userDto, fileName);
        return userDto;
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
    public List<UserDto> findFavorites(Long userTgId) {
        User user = findUserByTgId(userTgId);
        Set<User> usersWhoLikes = user.getLikes();
        Set<User> usersWhoIsLike = user.getLikeBy();

        List<UserDto> userDtoList = usersWhoIsLike.stream()
                .filter(user1 -> !usersWhoLikes.contains(user1))
                .map(user1 -> {
                    UserDto userDto = userMapper.userToUserDto(user1);
                    userDto.setLoveSign(Values.YOU_ARE_LOVE);
                    return userDto;
                }).collect(Collectors.toList());
        userDtoList.addAll(
                usersWhoLikes.stream()
                        .map(user1 -> {
                            UserDto userDto = userMapper.userToUserDto(user1);
                            if (usersWhoIsLike.contains(user1)) {
                                userDto.setLoveSign(Values.MUTUAL_LOVE);
                            } else {
                                userDto.setLoveSign(Values.LOVE);
                            }
                            return userDto;
                        }).collect(Collectors.toList())
        );
        return userDtoList;
    }

    @Override
    public Page<UserDto> findUsersWithPageable(Long userTgId, int page, int size) {
        User user = findUserByTgId(userTgId);
        PageRequest pageable = PageRequest.of(page, size);
        List<Gender> genders = new ArrayList<>();
        List<LoveSearch> loveSearches = new ArrayList<>();
        addSearchCriteria(user, genders, loveSearches);

        Page<User> userPage = userRepository.findUsers(user.getId(), genders, loveSearches, pageable);
        if (userPage == null) {
            throw new BusinessLogicException(MessageFormat.format("Form for user Telegram Id: {0} not found", userTgId));
        }

        List<UserDto> listUserDto = userPage.getContent()
                .stream()
                .map(userMapper::userToUserDto).peek(userView1 -> {
                    String fileName = userView1.getFormFileName();
                    if (fileName == null) {
                        createBase64CodeFromUserForm(userView1, createFormAndSave(userView1));
                    } else {
                        createBase64CodeFromUserForm(userView1, fileName);
                    }
                }).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(listUserDto, pageable, userPage::getTotalElements);
    }

    @Override
    public UserDto update(UserDto userDto) {
        log.info("Update user, Telegram Id: {}", userDto.getUsertgid());
        User user = findUserByTgId(userDto.getUsertgid());
        String fileName = user.getFormFileName();
        if (!userDto.getFormTitle().equals(user.getFormTitle()) || !userDto.getFormDescription().equals(user.getFormDescription())) {
            fileWorker.deleteFileFromDisc(fileName);
            try {
                fileName = createUserForm(userDto);
                userDto.setFormFileName(fileName);
                userDto.setId(user.getId());
            } catch (Exception e) {
                log.error("Error wen create user form with Telegram Id {} \n Error message: {}", userDto.getUsertgid(), e.getMessage());
                if ((fileName = userDto.getFormFileName()) != null) {
                    fileWorker.deleteFileFromDisc(fileName);
                }
                throw new BusinessLogicException("Error create new user: " + e.getMessage());
            }
        }
        userDto.setId(user.getId());
        userDto.setFormFileName(fileName);
        User userNewData = userMapper.fromUserDto(userDto);
        userNewData.getLikes().addAll(user.getLikes());
        userNewData.getLikeBy().addAll(user.getLikeBy());
        userRepository.save(userNewData);
        createBase64CodeFromUserForm(userDto, fileName);
        return userDto;
    }

    private User findUserByTgId(Long userTgId) {
        log.info("Find user with UserName: {}", userTgId);
        return userRepository.findByUsertgid(userTgId).
                orElseThrow(
                        () -> new EntityNotFoundException("User not found with Telegram Id: " + userTgId));
    }

    private String createFormAndSave(UserDto userDto) {
        String fileName;
        log.info("Create user form when find user but not find his form, username: {}", userDto.getUsertgid());
        fileName = createUserForm(userDto);
        userDto.setFormFileName(fileName);
        userRepository.save(userMapper.fromUserDto(userDto));
        return fileName;
    }

    private String createUserForm(UserDto userDto) {
        log.info("Create form for user with UserName: {} id: {}", userDto.getUsertgid(), userDto.getId());
        String preRevolutionFormTitle = convertTextToPreRevolution.convert(userDto.getFormTitle());
        String preRevolutionFormDesc = convertTextToPreRevolution.convert(userDto.getFormDescription());
        String fileName = usersForm.createUserForm(userDto.getUsertgid(),
                preRevolutionFormTitle,
                preRevolutionFormDesc);

        log.info("Save form on user with formName: {} username: {}", fileName, userDto.getUsertgid());
        return fileName;
    }

    private void createBase64CodeFromUserForm(UserDto userDto, String fileName) {
        log.info("Code attach in Base64");
        String formBase64 = fileWorker.getUserFormInBase64Format(fileName);
        log.info("Save attach in base64 UserName:" + userDto.getUsertgid());
        userDto.setAttachBase64Code(formBase64);
    }

    private void addSearchCriteria(User user, List<Gender> genders, List<LoveSearch> loveSearches) {
        switch (user.getLoveSearch().getCode()) {
            case "male":
                genders.add(Gender.MALE);
                break;
            case "females":
                genders.add(Gender.FEMALE);
                break;
            default:
                genders.add(Gender.MALE);
                genders.add(Gender.FEMALE);
                break;
        }
        if (user.getGender().getCode().equals("male")) {
            loveSearches.add(LoveSearch.MALES);
        } else {
            loveSearches.add(LoveSearch.FEMALES);
        }
        loveSearches.add(LoveSearch.ALL);
    }
}