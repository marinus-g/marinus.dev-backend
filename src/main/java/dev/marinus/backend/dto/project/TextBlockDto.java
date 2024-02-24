package dev.marinus.backend.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonTypeName("text")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TextBlockDto extends ContentBlockDto {

    public TextBlockDto(Long id, String text, boolean html) {
        super(id);
        this.text = text;
        this.html = html;
    }

    @JsonProperty("text")
    private String text;

    @JsonProperty("isHtml")
    private boolean html;
}
