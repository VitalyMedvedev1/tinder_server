package ru.liga.homework.service;

import org.springframework.stereotype.Service;
import ru.liga.homework.api.LikeUserService;

@Service
public class DefaultLikeUserService implements LikeUserService {
    @Override
    public void likeUser(Long userWhoLikes, Long userWhoIsLike) {
    }
}
