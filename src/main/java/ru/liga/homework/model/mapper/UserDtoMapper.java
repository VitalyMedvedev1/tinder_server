package ru.liga.homework.model.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.liga.homework.model.UserDto;
import ru.liga.homework.repository.entity.User;

@Mapper(componentModel = "spring", uses = User.class)
@SuppressWarnings("All")
public interface UserDtoMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    UserDto userToUserDto(User user);
}