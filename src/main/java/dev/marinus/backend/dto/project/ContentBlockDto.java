package dev.marinus.backend.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "block_type")
@JsonSubTypes({
        @JsonSubTypes.Type(TextBlockDto.class), @JsonSubTypes.Type(PictureBlockDto.class)})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class ContentBlockDto {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public Long id;

}