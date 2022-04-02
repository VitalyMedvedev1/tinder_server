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

//    @Query(value = "select * from otpmm.users where username != ?1 and gender in ?2", nativeQuery = true)

/*    @Query(value = "select at.file_name, u.id, u.gender from otpmm.users u inner join otpmm.user_attach at on u.id = at.id where u.id != ?1 and u.id in (select ul.user_id_that from otpmm.users_like ul where ul.user_id_who != ?1) and " +
            "(gender = (select case when cu.lookingfor='males' then 'male' when cu.lookingfor='females' then 'female' else 'male' end from otpmm.users cu where id = ?1) " +
            "or gender = ( select case when cu.lookingfor='males' then 'male' when cu.lookingfor='females' then 'female' else 'female' end from otpmm.users cu where id = ?1))", nativeQuery = true)
        List<User> findUsers(@Param("username") String username,  PageRequest pageable);   */

/*    @Query(value = "select u.* from otpmm.users u inner join otpmm.user_attach at on u.id = at.id where u.id != ?1 and u.id in (select ul.user_id_that from otpmm.users_like ul where ul.user_id_who != ?1) and " +
            "(gender = (select case when cu.lookingfor='males' then 'male' when cu.lookingfor='females' then 'female' else 'male' end from otpmm.users cu where id = ?1) " +
            "or gender = ( select case when cu.lookingfor='males' then 'male' when cu.lookingfor='females' then 'female' else 'female' end from otpmm.users cu where id = ?1))", nativeQuery = true)
        List<User> findUsers(@Param("username") Integer userId,  PageRequest pageable);   */

    @Query(value = "SELECT U.* FROM OTPMM.USERS U INNER JOIN OTPMM.USER_ATTACH AT ON U.ID = AT.ID WHERE U.ID != ?1 AND U.ID IN (SELECT UL.USER_ID_THAT FROM OTPMM.USERS_LIKE UL WHERE UL.USER_ID_WHO != ?1) AND " +
            "(GENDER = (SELECT CASE WHEN CU.LOOKINGFOR='MALES' THEN 'MALE' WHEN CU.LOOKINGFOR='FEMALES' THEN 'FEMALE' ELSE 'MALE' END FROM OTPMM.USERS CU WHERE ID = ?1) " +
            "OR GENDER = (SELECT CASE WHEN CU.LOOKINGFOR='MALES' THEN 'MALE' WHEN CU.LOOKINGFOR='FEMALES' THEN 'FEMALE' ELSE 'FEMALE' END FROM OTPMM.USERS CU WHERE ID = ?1))", nativeQuery = true)
        List<User> findUsers(@Param("userid") Integer userId,  PageRequest pageable);
}