package ru.giv13.tasktracker.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UserAwareRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findAllByUserId(Integer userId);

    Optional<T> findByIdAndUserId(ID id, Integer userId);
}
