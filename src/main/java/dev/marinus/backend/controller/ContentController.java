package dev.marinus.backend.controller;

import dev.marinus.backend.dto.ContentProfileCreateDto;
import dev.marinus.backend.dto.ContentProfileDto;
import dev.marinus.backend.dto.content.ContentCreateRequestDto;
import dev.marinus.backend.dto.content.ContentDto;
import dev.marinus.backend.model.entity.content.Content;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.user.RegisteredUser;
import dev.marinus.backend.service.ContentService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    private final ContentService contentService;


    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ContentProfileDto> getContentProfile(@AuthenticationPrincipal ContentProfile contentProfile) {
        return Optional.ofNullable(contentProfile)
                .map(contentService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profile/all")
    public ResponseEntity<List<ContentProfileDto>> getAllContentProfiles(@AuthenticationPrincipal RegisteredUser user) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.contentService.findAllContentProfiles().stream()
                .map(this.contentService::convertToDto)
                .toList());
    }

    @PostMapping("/profile")
    public ResponseEntity<ContentProfileDto> createContentProfile(@AuthenticationPrincipal RegisteredUser user,
                                                                 @RequestBody ContentProfileCreateDto dto) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (this.contentService.existsByName(dto.getName())) {
            return ResponseEntity.ok().build();
        }
        return this.contentService.createContentProfile(dto)
                .map(this.contentService::convertToDto)
                .map(contentProfileDto -> {
                    try {
                        return ResponseEntity.created(new URI("https/marinus.dev")).body(contentProfileDto);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/fetch/{profile}")
    public ResponseEntity<List<ContentDto>> getContentForProfile(@PathVariable ContentProfile profile) {
        return Optional.ofNullable(profile)
                .map(ContentProfile::getContent)
                .map(contents -> contents.stream().map(contentService::convertToDto).toList())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Integer>> getAvailableContentIds(@AuthenticationPrincipal ContentProfile profile) {
       final Optional<ContentProfile> contentProfile = Optional.ofNullable(profile);
        return contentProfile.map(ContentProfile::getContent)
                .map(contents -> contents.stream().map(content -> content.getId().intValue()).toList())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContent(@PathVariable Long id,
                                        @AuthenticationPrincipal ContentProfile profile) {
        final Optional<ContentProfile> contentProfile = Optional.ofNullable(profile);
        return contentProfile.map(ContentProfile::getContent)
                .map(contents -> contents.stream().filter(content -> content.getId().equals(id)).findFirst())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fetch/")
    public ResponseEntity<List<ContentDto>> getContent(
            @AuthenticationPrincipal ContentProfile profile) {
        System.out.println("fetching");
        final Optional<ContentProfile> contentProfile = Optional.ofNullable(profile);
        return contentProfile.map(ContentProfile::getContent)

                .map(content -> content.stream()
                        .map(this.contentService::convertToDto).toList())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/default")
    public ResponseEntity<Set<ContentDto>> getDefaultContent() {
        return ResponseEntity.ok(this.contentService.findDefaultContent().stream()
                .map(this.contentService::convertToDto).collect(Collectors.toSet()));
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<List<ContentDto>> getAllContent(@AuthenticationPrincipal RegisteredUser user) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.contentService.findAll().stream()
                .map(this.contentService::convertToDto)
                .toList());
    }
    @PostMapping(path = "/")
    public ResponseEntity<ContentDto> addContent(@AuthenticationPrincipal RegisteredUser user,
                                                 @RequestBody ContentCreateRequestDto createRequestDto) {
        System.out.println("in method");
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return this.contentService.createContent(createRequestDto)
                .map(this.contentService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());

    }

    @PutMapping("/")
    public ResponseEntity<ContentDto> updateContent(@AuthenticationPrincipal RegisteredUser user,
                                                    @RequestBody ContentDto dto) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return this.contentService.updateContent(dto)
                .map(this.contentService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<ContentProfileDto> updateContentProfile(@AuthenticationPrincipal RegisteredUser user,
                                                                 @RequestBody ContentProfileDto dto) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return this.contentService.updateContentProfile(dto)
                .map(this.contentService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContent(@AuthenticationPrincipal RegisteredUser user,
                                          @PathVariable Long id) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (this.contentService.deleteContent(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/profile/{profile}")
    public ResponseEntity<?> deleteContentProfile(@AuthenticationPrincipal RegisteredUser user,
                                                 @PathVariable ContentProfile profile) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (Optional.ofNullable(profile).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (this.contentService.deleteContentProfile(profile)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profile/{profile}/content/{content}")
    public ResponseEntity<?> addContentToProfile(@AuthenticationPrincipal RegisteredUser user,
                                                 @PathVariable ContentProfile profile,
                                                 @PathVariable Content<?> content) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (Optional.ofNullable(profile).isEmpty() || Optional.ofNullable(content).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

       return ResponseEntity
               .ok(this.contentService.convertToDto(this.contentService.addContentToProfile(profile, content)));
    }

    @DeleteMapping("/profile/{profile}/content/{content}")
    public ResponseEntity<?> removeContentFromProfile(@AuthenticationPrincipal RegisteredUser user,
                                                      @PathVariable ContentProfile profile,
                                                      @PathVariable Content<?> content) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (Optional.ofNullable(profile).isEmpty() || Optional.ofNullable(content).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok(this.contentService.convertToDto(this.contentService.removeContentFromProfile(profile, content)));
    }
}