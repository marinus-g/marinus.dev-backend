package dev.marinus.backend.service;

import dev.marinus.backend.dto.project.*;
import dev.marinus.backend.model.entity.Authenticatable;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.project.*;
import dev.marinus.backend.repository.ProjectRepository;
import dev.marinus.backend.repository.ProjectTagRepository;
import io.micrometer.observation.ObservationFilter;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTagRepository projectTagRepository;
    private final PictureService pictureService;
    private final ContentService contentService;

    public ProjectService(ProjectRepository projectRepository, ProjectTagRepository projectTagRepository,
                          PictureService pictureService, ContentService contentService) {
        this.projectRepository = projectRepository;
        this.projectTagRepository = projectTagRepository;
        this.pictureService = pictureService;
        this.contentService = contentService;
    }


    public Optional<ProjectDto> createProject(ProjectDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            return Optional.empty();
        }
        Project project = new Project();
        applyDtoToProject(dto, project);
        return Optional.of(toDto(projectRepository.save(project)));
    }

    private void applyDtoToProject(ProjectDto dto, Project project) {
        project.setName(dto.getName());
        project.setLink(dto.getLink());
        project.setDifficulty(dto.getDifficulty());
        project.setTags(dto.getTags() == null ? new ArrayList<>() : dto.getTags()
                .stream()
                .map(tag -> this.projectTagRepository.findByTagIgnoreCase(tag).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        project.setContentProfiles(dto.getContentProfiles() == null ? new ArrayList<>() : dto.getContentProfiles()
                .stream()
                .map(contentProfileId -> this.contentService.findById(contentProfileId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        project.setProjectDescription(new ProjectDescription(dto.getProjectDescription().getMarkdown()));
        project.setThumbnailReference(dto.getThumbnailReference());
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Project> findAllByContentProfilesContaining(Optional<Authenticatable> contentProfile) {
        return contentProfile
                .filter(authenticatable -> authenticatable instanceof ContentProfile)
                .map(authenticatable -> projectRepository.findAllByContentProfilesContaining((ContentProfile) authenticatable))
                .orElseGet(this.projectRepository::findByContentProfilesEmpty);
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public TagDto createTag(TagDto tagDto) {
        this.projectTagRepository.findByTagIgnoreCase(tagDto.getTag()).ifPresentOrElse(projectTag -> {
            tagDto.setId(projectTag.getId());
            tagDto.setProjectCount(projectTag.getProjects().size());
        }, () -> {
            ProjectTag tag = this.projectTagRepository.save(new ProjectTag(tagDto.getTag()));
            tagDto.setId(tag.getId());
            tagDto.setProjectCount(0);
        });
        return tagDto;
    }

    public boolean tagExists(String tag) {
        return this.projectTagRepository.existsByTagIgnoreCase(tag);
    }

    public List<TagDto> getTags() {
        return this.projectTagRepository.findAll()
                .stream()
                .map(tag -> new TagDto(tag.getId(), tag.getTag(), tag.getProjects().size()))
                .toList();
    }

    public Optional<TagDto> getTag(Long id) {
        return this.projectTagRepository.findById(id)
                .map(tag -> new TagDto(tag.getId(), tag.getTag(), tag.getProjects().size()));
    }

    public boolean deleteTag(Long id) {
        if (this.projectTagRepository.existsById(id)) {
            this.projectTagRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Boolean> deleteProject(Long id) {
        if (this.projectRepository.existsById(id)) {
            this.projectRepository.deleteById(id);
            return Optional.of(true);
        }
        return Optional.empty();
    }

    public Optional<ProjectDto> updateProject(ProjectDto projectDto) {
        return this.projectRepository.findById(projectDto.getId())
                .map(project -> {
                    pictureService.deletePicture(project.getThumbnailReference());
                    applyDtoToProject(projectDto, project);
                    return toDto(this.projectRepository.save(project));
                });
    }

    public ProjectDto toDto(Project project) {
        final ProjectDescriptionDto descriptionDto = new ProjectDescriptionDto(
                project.getProjectDescription().getMarkdown()
        );
        return new ProjectDto(
                project.getId(),
                project.getName(),
                descriptionDto,
                project.getLink(),
                project.getDifficulty(),
                project.getThumbnailReference(),
                project.getContentProfiles()
                        .stream()
                        .map(ContentProfile::getId)
                        .toList(),
                project.getTags()
                        .stream()
                        .map(ProjectTag::getTag)
                        .toList()
        );
    }
}