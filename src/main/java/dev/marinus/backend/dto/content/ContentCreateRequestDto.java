package dev.marinus.backend.dto.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "content_type")
@JsonSubTypes({
        @JsonSubTypes.Type(ContentWelcomeMessageCreateRequestDto.class),}
)
public abstract class ContentCreateRequestDto {

    private String name;

}
