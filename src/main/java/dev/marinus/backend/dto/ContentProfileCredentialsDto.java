package dev.marinus.backend.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.marinus.backend.model.Credentials;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonTypeName("content")
public class ContentProfileCredentialsDto extends Credentials {

    public ContentProfileCredentialsDto(String userName) {
        super(userName);
    }

}