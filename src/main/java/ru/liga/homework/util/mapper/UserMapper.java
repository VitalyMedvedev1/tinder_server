package ru.liga.homework.util.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;
import ru.liga.homework.db.entity.User;
import ru.liga.homework.model.User.UserView;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserView map(User user) {
        modelMapper.getConfiguration()
                .setCollectionsMergeEnabled(false)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
/*        PropertyMap<UserView, User> skip = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip().setLikes(null);
                skip().setLikeBy(null);
            }
        };
        modelMapper.addMappings(skip);*/
        return modelMapper.map(user, UserView.class);
    }
}