package dev.marinus.backend.controller;

import dev.marinus.backend.dto.ContentProfileDto;
import dev.marinus.backend.dto.content.ContentCreateRequestDto;
import dev.marinus.backend.dto.content.ContentDto;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.user.RegisteredUser;
import dev.marinus.backend.service.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/fetch")
    public ResponseEntity<List<ContentDto>> getContent(
            @AuthenticationPrincipal ContentProfile profile) {
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

    @PostMapping(path = "/", consumes = "application/json")
    public ResponseEntity<ContentDto> addContent(@AuthenticationPrincipal RegisteredUser user,
                                                 @RequestBody ContentCreateRequestDto createRequestDto) {
        if (Optional.ofNullable(user).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return this.contentService.createContent(createRequestDto)
                .map(this.contentService::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());

    }
}