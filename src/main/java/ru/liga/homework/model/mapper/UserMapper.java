package ru.liga.homework.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.liga.homework.model.UserDto;
import ru.liga.homework.repository.entity.User;

@Mapper(componentModel = "spring", uses = User.class)
@SuppressWarnings("All")
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Named("userToUserDto")
    @Mappings({
            @Mapping(target = "likes", expression = "java(null)"),
            @Mapping(target = "likeBy", expression = "java(null)")})
    UserDto userToUserDto(User user);

    User fromUserDto(UserDto userDto);
}