package dev.marinus.backend.model.entity.project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PictureBlock extends ContentBlock {

    @Column(name = "picture")
    private String picture;
    @Column(name = "is_url")
    private boolean url;
    @Column(name = "image_type")
    private String imageType;

}