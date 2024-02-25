package dev.marinus.backend.model.entity.project;

import dev.marinus.backend.model.entity.content.profile.ContentProfile;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "thumbnail_reference")
    private String thumbnailReference;

    @Column(name = "name")
    private String name;

    @Embedded
    private ProjectDescription projectDescription;

    @Column(name = "link")
    private String link;

    @Column(name = "difficulty")
    private int difficulty;

    @ManyToMany
    private List<ContentProfile> contentProfiles;

    @ManyToMany
    private List<ProjectTag> tags;

    @Transactional
    public void addTag(ProjectTag tag) {
        tags.add(tag);
    }

    @Transactional
    public void removeTag(ProjectTag tag) {
        tags.remove(tag);
    }

    @Transactional
    public void addContentProfile(ContentProfile profile) {
        contentProfiles.add(profile);
    }

    @Transactional
    public void removeContentProfile(ContentProfile profile) {
        contentProfiles.remove(profile);
    }
}