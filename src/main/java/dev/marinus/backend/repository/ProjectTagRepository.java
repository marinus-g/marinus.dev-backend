package dev.marinus.backend.repository;

import dev.marinus.backend.model.entity.project.ProjectTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {
    Optional<ProjectTag> findByTagIgnoreCase(String tag);

    boolean existsByTagIgnoreCase(String tag);

}