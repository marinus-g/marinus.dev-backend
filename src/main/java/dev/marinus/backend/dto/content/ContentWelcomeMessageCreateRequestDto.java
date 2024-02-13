package dev.marinus.backend.dto.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("welcome_screen")
public class ContentWelcomeMessageCreateRequestDto extends ContentCreateRequestDto {

    private List<String> welcomeMessage;

}