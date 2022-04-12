package ru.liga.homework.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;
import ru.liga.homework.repository.entity.User;
import ru.liga.homework.model.UserDto;

@Component
@RequiredArgsConstructor
public class UserModelMapper {

    private final ModelMapper modelMapper;

    public UserDto map(User user) {
        modelMapper.getConfiguration()
                .setCollectionsMergeEnabled(false)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper.map(user, UserDto.class);
    }
}