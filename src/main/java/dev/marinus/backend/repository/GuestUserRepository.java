package dev.marinus.backend.repository;

import dev.marinus.backend.model.user.GuestUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestUserRepository extends JpaRepository<GuestUser, Long> {

    Optional<GuestUser> findByUsernameEqualsIgnoreCase(String userName);

    Optional<GuestUser> findByUsername(String userName);

}
