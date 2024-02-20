package dev.marinus.backend.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("picture")
@NoArgsConstructor
@Getter
@Setter
public class PictureBlockDto extends ContentBlockDto {

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("isUrl")
    private boolean url;

    @JsonProperty("imageType")
    private String imageType;

}
