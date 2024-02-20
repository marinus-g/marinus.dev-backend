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
public class TextBlock extends ContentBlock {

    @Column(name = "text")
    private String text;
    @Column(name = "is_html")
    private boolean html;


}
