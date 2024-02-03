package dev.marinus.backend.dto;

import dev.marinus.backend.model.Credentials;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContentProfileCredentialsDto extends Credentials {

    public ContentProfileCredentialsDto(String userName) {
        super(userName);
    }
}