package dev.marinus.backend.repository;

import dev.marinus.backend.model.entity.content.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content<?>, Long> {

}
