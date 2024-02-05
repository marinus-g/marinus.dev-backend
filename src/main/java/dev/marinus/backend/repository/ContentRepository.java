package dev.marinus.backend.repository;

import dev.marinus.backend.model.content.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content<?>, Long> {

}
