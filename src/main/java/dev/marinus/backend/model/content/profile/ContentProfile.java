package dev.marinus.backend.model.content.profile;

import dev.marinus.backend.model.Authenticatable;
import dev.marinus.backend.model.content.Content;
import dev.marinus.backend.model.content.type.ContentType;
import dev.marinus.backend.model.theme.Theme;
import dev.marinus.backend.model.user.RegisteredUser;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "content_profile")
@NoArgsConstructor
@Setter
@Getter
public abstract class ContentProfile implements Authenticatable {

    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @Nullable
    private Theme theme;
    @ManyToOne
    @Nullable
    private RegisteredUser user;

    @Column(name = "token_id", unique = false)
    @NonNull
    private UUID tokenId;

    @Column(name = "content_profile_type" )
    private ContentProfileType contentProfileType;


    @OneToMany(mappedBy = "id")
    private Set<Content<? extends ContentType>> content = new HashSet<>();

    public void addContent(Content<? extends ContentType> content) {
        this.content.add(content);
    }

    public void removeContent(Content<? extends ContentType> content) {
        this.content.remove(content);
    }

    public void removeContent(long id) {
        this.content.removeIf(content -> content.getId() == id);
    }

}