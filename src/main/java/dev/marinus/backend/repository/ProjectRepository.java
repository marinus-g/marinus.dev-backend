package dev.marinus.backend.repository;

import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.project.Project;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByContentProfilesContaining(@Nonnull ContentProfile contentProfile);

    List<Project> findByContentProfilesContainsOrContentProfilesEmpty(@NonNull ContentProfile contentProfiles);

    List<Project> findByContentProfilesEmpty();

    boolean existsByThumbnailReference(String thumbnailReference);

}