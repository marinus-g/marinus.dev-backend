package dev.marinus.backend.dto.content;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("welcome_screen")
public class WelcomeScreenContentDto extends ContentDto {

    private List<String> welcomeMessage = new ArrayList<>();
    private int weight = 0;


    public WelcomeScreenContentDto(Long id, String name, List<String> welcomeMessage, int weight) {
        super(id, name);
        this.welcomeMessage = welcomeMessage;
        this.weight = weight;
    }
}
