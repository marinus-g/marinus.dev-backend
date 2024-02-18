package dev.marinus.backend.dto;

import dev.marinus.backend.model.entity.content.profile.ContentProfileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContentProfileCreateDto {

    private String name;
    private ContentProfileType contentProfileType;
    private GuestUserDto guestUser;

}