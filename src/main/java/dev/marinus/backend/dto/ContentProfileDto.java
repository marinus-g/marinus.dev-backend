package dev.marinus.backend.dto;

import dev.marinus.backend.model.content.profile.ContentProfileType;
import dev.marinus.backend.model.user.GuestUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@Getter
@Setter
public class ContentProfileDto {

    private @Nullable Long id;
    private String name;
    private ContentProfileType contentProfileType;
    private ThemeDto themeDto;
    private @Nullable GuestUser guestUser;

    public ContentProfileDto(@Nullable long id, String name, ContentProfileType contentProfileType,
                             ThemeDto themeDto, @Nullable GuestUser guestUser) {
        this.id = id;
        this.name = name;
        this.contentProfileType = contentProfileType;
        this.themeDto = themeDto;
        this.guestUser = guestUser;
    }
}