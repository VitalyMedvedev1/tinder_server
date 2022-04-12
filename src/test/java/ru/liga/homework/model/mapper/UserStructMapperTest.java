//package ru.liga.homework.model.mapper;
//
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.liga.homework.model.UserDto;
//import ru.liga.homework.repository.UserRepository;
//import ru.liga.homework.repository.entity.User;
//import ru.liga.homework.type.Gender;
//import ru.liga.homework.type.LoveSearch;
//
//import java.util.HashSet;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//class UserStructMapperTest {
//
//    ModelMapper modelMapper = new ModelMapper();
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    void shouldMapUserToUserDto() {
//        User user = new User(0L, 0L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
//
//        UserDto userDto = UserStructMapper.INSTANCE.UserToUserElement(user);
//
//        assertTrue(true);
//    }
//
//    @Test
//    void mapUserWithCycleSetLikes() {
//        User user0 = new User(0L, 0L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
//        User user1 = new User(1L, 1L, "1", "1", Gender.MALE, "1", LoveSearch.ALL, "1", "1", new HashSet<>(), new HashSet<>());
//        user0.getLikes().add(user1);
//        user0.getLikeBy().add(user1);
//
//        UserDto userDto = modelMapper.map(user0, UserDto.class);
//
//        assertTrue(true);
//    }
//    @Test
//    void mapUserWithCycleSetLikes1() {
//
//        User user = userRepository.findByUsertgid(99901L).get();
//        user.getLikes();
//
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//
//        assertTrue(true);
//    }
//}