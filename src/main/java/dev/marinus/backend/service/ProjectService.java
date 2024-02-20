package dev.marinus.backend.service;

import dev.marinus.backend.dto.project.ProjectDto;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import dev.marinus.backend.model.entity.project.Project;
import dev.marinus.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }



    public Optional<Project> createProject(ProjectDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            return Optional.empty();
        }
        Project project = new Project();
        project.setName(dto.getName());
        return Optional.of(projectRepository.save(project));
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
}