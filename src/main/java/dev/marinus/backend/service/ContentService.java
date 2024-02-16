package dev.marinus.backend.service;

import dev.marinus.backend.dto.ContentProfileDto;
import dev.marinus.backend.dto.content.ContentCreateRequestDto;
import dev.marinus.backend.dto.content.ContentDto;
import dev.marinus.backend.dto.content.ContentWelcomeMessageCreateRequestDto;
import dev.marinus.backend.dto.content.WelcomeScreenContentDto;
import dev.marinus.backend.model.entity.content.Content;
import dev.marinus.backend.model.entity.content.impl.WelcomeScreenContent;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.content.profile.ContentProfileType;
import dev.marinus.backend.model.entity.content.type.ContentType;
import dev.marinus.backend.model.entity.user.GuestUser;
import dev.marinus.backend.repository.ContentProfileRepository;
import dev.marinus.backend.repository.ContentRepository;
import dev.marinus.backend.util.DefaultUtil;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentProfileRepository contentProfileRepository;
    private final UserService userService;
    private final ThemeService themeService;

    public ContentService(ContentRepository contentRepository, ContentProfileRepository contentProfileRepository,
                          UserService userService, ThemeService themeService) {
        this.contentRepository = contentRepository;
        this.contentProfileRepository = contentProfileRepository;
        this.userService = userService;
        this.themeService = themeService;
        this.init();
    }


    public Optional<ContentProfile> createContentProfile(ContentProfileDto dto) {
        ContentProfile contentProfile = new ContentProfile();
        contentProfile.setName(dto.getName());
        contentProfile.setContentProfileType(dto.getContentProfileType());
        contentProfile.setUser(dto.getGuestUser());
        contentProfile.setTheme(Optional.ofNullable(dto.getThemeDto()).map(themeService::convertToEntity).orElse(null));
        do {
            contentProfile.setTokenId(UUID.randomUUID());
        } while (contentProfileRepository.findByTokenId(contentProfile.getTokenId()).isPresent());
        return Optional.of(contentProfileRepository.save(contentProfile));
    }

    public Optional<ContentProfile> findContentProfileByUuid(UUID tokenId) {
        return contentProfileRepository.findByTokenId(tokenId);
    }

    public ContentProfileDto convertToDto(@NonNull ContentProfile contentProfile) {
        return new ContentProfileDto(
                contentProfile.getId(),
                contentProfile.getName(),
                contentProfile.getContentProfileType(),
                Optional.ofNullable(contentProfile.getTheme()).map(themeService::convertToDto).orElse(null),
                contentProfile.getUser()
        );
    }

    public Set<Content<?>> findDefaultContent() {
        return this.contentProfileRepository.findById(1L)
                .map(ContentProfile::getContent)
                .orElseThrow(() -> new IllegalStateException("Default content profile not found"));
    }

    public ContentDto convertToDto(@NonNull Content<? extends ContentType> content) {
        if (content instanceof WelcomeScreenContent welcomeScreenContent) {
            return new WelcomeScreenContentDto(
                    content.getId(),
                    content.getName(),
                    welcomeScreenContent.getWelcomeMessage()
            );
        }
        throw new IllegalArgumentException("Unknown content type");
    }

    private void init() {
        ContentProfileDto contentProfileDto = new ContentProfileDto();
        contentProfileDto.setName("Default");
        contentProfileDto.setContentProfileType(ContentProfileType.COMPANY);
        GuestUser guestUser = new GuestUser(true);
        guestUser.setUsername("guest");
        guestUser = this.userService.saveGuestUser(guestUser);
        contentProfileDto.setGuestUser(guestUser);
        this.createContentProfile(contentProfileDto);
        this.contentProfileRepository.findById(1L).ifPresent(contentProfile -> {
            WelcomeScreenContent welcomeScreenContent = DefaultUtil.createDefaultWelcomeScreenContent();
            welcomeScreenContent = this.contentRepository.save(welcomeScreenContent);
            contentProfile.addContent(welcomeScreenContent);
            this.contentProfileRepository.save(contentProfile);
        });
    }

    public Optional<Content<?>> createContent(ContentCreateRequestDto createRequestDto) {
        if (createRequestDto instanceof ContentWelcomeMessageCreateRequestDto welcomeScreenContentDto) {
            WelcomeScreenContent welcomeScreenContent = new WelcomeScreenContent();
            welcomeScreenContent.setName(welcomeScreenContentDto.getName());
            welcomeScreenContent.setWelcomeMessage(welcomeScreenContentDto.getWelcomeMessage());
            return Optional.of(contentRepository.save(welcomeScreenContent));
        }
        return Optional.empty();
    }

    public Collection<Content<?>> findAll() {
        return this.contentRepository.findAll();
    }
}