package dev.marinus.backend.model.entity.theme;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TerminalTheme {
    @Embedded
    private Ps1 ps1;
    @Embedded
    private Font font;
    private String outlineColor;
    private String outerColor;
    private String backgroundColor;
    private String primaryColor;
    private String secondaryColor;
    private String informationColor;
    private String warningColor;
    private String highlightColor;
    private String clickableColor;

}