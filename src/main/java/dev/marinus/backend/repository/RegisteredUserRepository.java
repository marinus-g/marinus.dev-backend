package dev.marinus.backend.repository;

import dev.marinus.backend.model.entity.user.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

    Optional<RegisteredUser> findByUsernameEqualsIgnoreCase(String userName);

    Optional<RegisteredUser> findByUsername(String userName);
}
