package ru.liga.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.homework.db.entity.Attach;

import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {
    Optional<Attach> findByUserId(Integer userId);
}
