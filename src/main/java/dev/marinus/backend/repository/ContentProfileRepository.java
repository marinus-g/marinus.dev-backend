package dev.marinus.backend.repository;

import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentProfileRepository extends JpaRepository<ContentProfile, Long> {

    Optional<ContentProfile> findByTokenId(UUID tokenId);

    Optional<ContentProfile> findByName(String name);

    boolean existsByName(String name);

}
