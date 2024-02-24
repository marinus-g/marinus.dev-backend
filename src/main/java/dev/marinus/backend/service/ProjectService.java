package dev.marinus.backend.service;

import dev.marinus.backend.dto.project.*;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.project.*;
import dev.marinus.backend.repository.ProjectRepository;
import dev.marinus.backend.repository.ProjectTagRepository;
import io.micrometer.observation.ObservationFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        project.setName(dto.getName());
        return Optional.of(toDto(projectRepository.save(project)));
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Project> findAllByContentProfilesContaining(ContentProfile contentProfile) {
        return projectRepository.findAllByContentProfilesContaining(contentProfile);
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
                    project.setName(projectDto.getName());
                    project.setLink(projectDto.getLink());
                    project.setDifficulty(projectDto.getDifficulty());
                    project.setTags(projectDto.getTags()
                            .stream()
                            .map(tag -> this.projectTagRepository.findByTagIgnoreCase(tag).orElse(null))
                            .filter(Objects::nonNull)
                            .toList()
                    );
                    project.setContentProfiles(projectDto.getContentProfiles()
                            .stream()
                            .map(contentProfileId -> this.contentService.findById(contentProfileId).orElse(null))
                            .filter(Objects::nonNull)
                            .toList());
                    project.setProjectDescription(new ProjectDescription(
                            projectDto.getProjectDescription().getContentBlocks()
                                    .stream()
                                    .map(this::fromDto)
                                    .toList()
                    ));
                    project.setThumbnail(new PictureBlock(
                            projectDto.getThumbnail().getPicture(),
                            projectDto.getThumbnail().isUrl(),
                            projectDto.getThumbnail().getImageType()
                    ));
                    return toDto(this.projectRepository.save(project));
                });
    }

    private ProjectDto toDto(Project project) {
        final ProjectDescriptionDto descriptionDto = new ProjectDescriptionDto(
                project.getProjectDescription().getContentBlocks()
                        .stream()
                        .map(this::toDto)
                        .toList()
        );
        final PictureBlockDto thumbnailDto = new PictureBlockDto();
        return new ProjectDto(
                project.getId(),
                project.getName(),
                descriptionDto,
                project.getLink(),
                project.getDifficulty(),
                thumbnailDto,
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

    private ContentBlockDto toDto(ContentBlock contentBlock) {
        switch (contentBlock.getClass().getSimpleName()) {
            case "PictureBlock" -> {
                PictureBlock pictureBlock = (PictureBlock) contentBlock;
                return new PictureBlockDto(pictureBlock.getPicture(), pictureBlock.isUrl(), pictureBlock.getImageType());
            }
            case "TextBlock" -> {
                TextBlock textBlock = (TextBlock) contentBlock;
                return new TextBlockDto(textBlock.getText(), textBlock.isHtml());
            }
            default -> throw new IllegalArgumentException("Unknown content block type");
        }
    }

    private ContentBlock fromDto(ContentBlockDto contentBlockDto) {
        switch (contentBlockDto.getClass().getSimpleName()) {
            case "PictureBlockDto" -> {
                PictureBlockDto pictureBlockDto = (PictureBlockDto) contentBlockDto;
                return new PictureBlock(pictureBlockDto.getPicture(), pictureBlockDto.isUrl(), pictureBlockDto.getImageType());
            }
            case "TextBlockDto" -> {
                TextBlockDto textBlockDto = (TextBlockDto) contentBlockDto;
                return new TextBlock(textBlockDto.getText(), textBlockDto.isHtml());
            }
            default -> throw new IllegalArgumentException("Unknown content block type");
        }
    }
}