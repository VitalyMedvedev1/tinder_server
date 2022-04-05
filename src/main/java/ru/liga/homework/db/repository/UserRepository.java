package ru.liga.homework.db.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.liga.homework.db.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT " +
                            "U.* " +
                            "FROM OTPMM.USERS U " +
                            "WHERE U.ID != ?1 AND " +
                            "U.ID NOT IN (SELECT UL.USER_ID_THAT FROM OTPMM.USERS_LIKE UL WHERE UL.USER_ID_WHO = ?1) AND " +
                            "(GENDER IN ?2) AND " +
                            "(LOOKINGFOR in ?3)", nativeQuery = true)
    List<User> findUsers(@Param("userid") Integer userId, List<String> genders, List<String> lookingfor,  PageRequest pageable);
}