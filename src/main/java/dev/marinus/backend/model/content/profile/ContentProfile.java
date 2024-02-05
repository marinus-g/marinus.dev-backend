package dev.marinus.backend.model.content.profile;

import dev.marinus.backend.model.Authenticatable;
import dev.marinus.backend.model.content.Content;
import dev.marinus.backend.model.content.type.ContentType;
import dev.marinus.backend.model.theme.Theme;
import dev.marinus.backend.model.user.GuestUser;
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
@Table(name = "content_profile")
@NoArgsConstructor
@Setter
@Getter
public class ContentProfile implements Authenticatable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "token_id", unique = false)
    @NonNull
    private UUID tokenId;

    @ManyToOne
    @Nullable
    private Theme theme;

    @ManyToOne
    @Nullable
    private GuestUser user;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "content_profile_type" )
    private ContentProfileType contentProfileType;

    @Column(name = "display_email")
    private boolean displayEmail;

    @Column(name = "is_deletable")
    private boolean isDeletable;

    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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