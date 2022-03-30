package ru.liga.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.liga.homework.db.entity.LikeUsers;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeUsers, Long> {

    @Query(nativeQuery = true, value = (
            "SELECT USERNAMESECOND " +
                    "FROM LIKEUSERS " +
                    "WHERE USERFIRSTFLG = TRUE AND USERSECOBDFLG = FALSE AND USERNAMEFIRST = :username"))
    List<String> findUsersWhoLikes(@Param("username") String userName);

    @Query(nativeQuery = true, value = (
            "SELECT USERNAMESECOND " +
                    "FROM LIKEUSERS " +
                    "WHERE USERFIRSTFLG = FALSE AND USERSECOBDFLG = TRUE AND USERNAMEFIRST = :username"))
    List<String> findUsersWhoLikeMe(@Param("username") String userName);

    @Query(nativeQuery = true, value = (
            "SELECT USERNAMESECOND " +
                    "FROM LIKEUSERS " +
                    "WHERE USERFIRSTFLG = TRUE AND USERSECOBDFLG = TRUE AND USERNAMEFIRST = :username"))
    List<String> findUsersWithMutual(@Param("username") String userName);

    @Query(nativeQuery = true, value =
            "SELECT * " +
                    "FROM LIKEUSERS " +
                    "WHERE (USERNAMEFIRST = :userNameWhoLike AND USERNAMESECOND = :userNameWhoWasLike) " +
                        "OR (USERNAMEFIRST = :userNameWhoWasLike AND USERNAMESECOND = :userNameWhoLike)")
    LikeUsers findLikesWithUserNames(@Param("userNameWhoLike") String userNameWhoLike,
                                     @Param("userNameWhoWasLike") String userNameWhoWasLike);
}