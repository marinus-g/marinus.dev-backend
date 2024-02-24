package dev.marinus.backend.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("picture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PictureBlockDto extends ContentBlockDto {

    /*
     if the picture is a url, the picture field will contain the url
        if the picture is a file, the picture field will contain the picture uid
     */
    @JsonProperty("picture")
    private String picture;

    @JsonProperty("isUrl")
    private boolean url;

    @JsonProperty("imageType")
    private String imageType;

}
