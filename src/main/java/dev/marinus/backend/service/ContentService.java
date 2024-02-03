package dev.marinus.backend.service;

import dev.marinus.backend.dto.ContentProfileDto;
import dev.marinus.backend.model.content.profile.ContentProfile;
import dev.marinus.backend.model.content.profile.impl.CompanyContentProfile;
import dev.marinus.backend.repository.ContentProfileRepository;
import dev.marinus.backend.repository.ContentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentProfileRepository contentProfileRepository;

    public ContentService(ContentRepository contentRepository, ContentProfileRepository contentProfileRepository) {
        this.contentRepository = contentRepository;
        this.contentProfileRepository = contentProfileRepository;
    }

    public Optional<ContentProfile> createContentProfile(ContentProfileDto dto) {
       return Optional.empty();
    }

}
