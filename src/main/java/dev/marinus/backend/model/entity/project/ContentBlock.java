package dev.marinus.backend.model.entity.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ContentBlock {

    @Id
    private Long id;

}