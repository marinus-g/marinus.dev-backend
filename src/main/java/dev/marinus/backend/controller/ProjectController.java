package dev.marinus.backend.controller;

import dev.marinus.backend.dto.project.ProjectDto;
import dev.marinus.backend.model.entity.Authenticatable;
import dev.marinus.backend.security.annotation.RequiresCommand;
import dev.marinus.backend.dto.project.TagDto;
import dev.marinus.backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/tag")
    @RequiresCommand("projects")
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        if (tagDto.getTag() == null || tagDto.getTag().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.projectService.createTag(tagDto));
    }

    @DeleteMapping("/tag/{id}")
    @RequiresCommand("projects")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        return Optional.of(this.projectService.deleteTag(id))
                .map(aBoolean -> aBoolean ? ResponseEntity.ok().build() : ResponseEntity.notFound().build())
                .orElseThrow(NullPointerException::new); // this should never happen
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getTags() {
        return ResponseEntity.ok(this.projectService.getTags());
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable Long id) {
        return this.projectService.getTag(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @RequiresCommand("projects")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        return this.projectService.createProject(projectDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/")
    @RequiresCommand("projects")
    public ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectDto projectDto) {
        return this.projectService.updateProject(projectDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @RequiresCommand("projects")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        return this.projectService.deleteProject(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectDto>> fetchAll(@AuthenticationPrincipal Authenticatable authenticatable) {
        return ResponseEntity.ok(this.projectService.findAllByContentProfilesContaining(Optional.ofNullable(authenticatable)).stream().map(this.projectService::toDto).toList());
    }
}