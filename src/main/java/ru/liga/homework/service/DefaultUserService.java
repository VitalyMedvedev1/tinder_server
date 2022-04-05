package ru.liga.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultUserService implements UserService {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final UsersFormService usersFormService;
    private final ConvertTextToPreRevolution convertTextToPreRevolution;
    private final FileWorker fileWorker;

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
        try {
            String fileName = createUserForm(userView);
            userView.setFormFileName(fileName);

            log.info("Save user with name: {}", userView.getName());

            EntityManager em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            User user;
            try {
                user = userRepository.save(modelMapper.map(userView, User.class));
                tx.commit();
            }
            catch (Exception e){
                tx.rollback();
                log.error("Error wen create new user with login {} \n Error message: {}", userView.getName(), e.getCause().getCause().getMessage());
                if ((fileName = userView.getFormFileName()) != null) {
                    fileWorker.deleteFileFromDisc(fileName);
                }
                throw new BusinessLogicException("Error create new user: " + e.getCause().getCause().getMessage());
            }

            //String fileName = createUserForm(userView);
            //user.setFormFileName(fileName);
            //userRepository.save(user);

            userView.setFormFileName(fileName);
            userView.setId(user.getId());
            createBase64CodeFromUserForm(userView, userView.getFormFileName());
        }
        catch (DataIntegrityViolationException e){
            log.error("Error wen create new user with login {} \n Error message: {}", userView.getName(), e.getCause().getCause().getMessage());
            String fileName;
            if ((fileName = userView.getFormFileName()) != null) {
               fileWorker.deleteFileFromDisc(fileName);
            }
            throw new BusinessLogicException("Error create new user: " + e.getCause().getCause().getMessage());
        }
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

    @Override
    public UserView update(UserView userView) {
        log.info("Update user, username: {}", userView.getUsername());
        userRepository.save(modelMapper.map(userView, User.class));
        return userView;
    }

    private User findUserByName(String userName) {
        log.info("Find user with name: {}", userName);
        return userRepository.findByUsername(userName).orElseThrow(
                () -> {
                    log.error("User not found with login: {}", userName);
                    return new BusinessLogicException("User not found with login: " + userName);
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
        String fileName = usersFormService.createUserForm(userView.getUsername(),
                                                            convertTextToPreRevolution.convert(userView.getHeader()),
                                                            convertTextToPreRevolution.convert(userView.getDescription()));

        log.info("Save form on user with formName: {} username: {}", fileName, userView.getUsername());
        return fileName;
    }

    private void createBase64CodeFromUserForm(UserView userView, String fileName) {
        log.info("Code attach in Base64");
        String formBase64 = fileWorker.getUserFormInBase64Format(fileName);
        log.info("Save attach in base64 UserView");
        userView.setAttachBase64Code(formBase64);
    }

    private void addSearchCriteria(User user, List<String> genders, List<String> lookingFor) {
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