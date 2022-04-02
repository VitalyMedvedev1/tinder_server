package ru.liga.homework.db.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.liga.homework.db.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);


/*    @Query(value = "SELECT * FROM OTPMM.USERS U WHERE U.USERNAME != :USERNAME AND U.GENDER = :LOOKINGFOR", nativeQuery = true)
    List<User> findUsers(@Param("USERNAME") String userName, @Param("LOOKINGFOR") String lookingFor, PageRequest pageable);*/
//ORDER BY ?#{#pageable}

    @Query(value = "select * from otpmm.users where username != ?1 and gender = ?2", nativeQuery = true)
        List<User> findUsers(@Param("username") String userName, @Param("gender") String gender, PageRequest pageable);
}