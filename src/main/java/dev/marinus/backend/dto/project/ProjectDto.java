package dev.marinus.backend.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDto {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Long id;

    private String name;

    private ProjectDescriptionDto projectDescription;

    private String link;

    private int difficulty;

    private PictureBlockDto thumbnail;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Long> contentProfiles;

    private List<String> tags;

}
