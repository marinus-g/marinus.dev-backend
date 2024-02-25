package dev.marinus.backend.model.entity.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDescription {

    @Column(name = "markdown", columnDefinition = "LONGTEXT")
    private String markdown;

}