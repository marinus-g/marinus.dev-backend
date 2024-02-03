package dev.marinus.backend.model.content;

import dev.marinus.backend.model.content.type.ContentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "content")
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Content<T extends ContentType> {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    private transient T type;

}