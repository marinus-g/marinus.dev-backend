package dev.marinus.backend.dto;

import dev.marinus.backend.model.theme.TerminalTheme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThemeDto {

    private String name;
    private TerminalTheme terminalTheme;

}
