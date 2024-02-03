package dev.marinus.backend.dto;

import dev.marinus.backend.model.content.Content;
import dev.marinus.backend.model.content.type.ContentType;
import dev.marinus.backend.model.user.RegisteredUser;

public abstract class ContentProfileDto {

    private Long id;
    private String name;
    private ContentType contentType;
    private ThemeDto themeDto;
    private RegisteredUser registeredUser;
}