package dev.marinus.backend.dto.project;

import dev.marinus.backend.model.entity.project.ContentBlock;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDescriptionDto {

    private List<ContentBlockDto> contentBlocks;

}