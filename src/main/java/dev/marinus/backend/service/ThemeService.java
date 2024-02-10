package dev.marinus.backend.service;

import dev.marinus.backend.dto.ThemeDto;
import dev.marinus.backend.model.entity.theme.Theme;
import dev.marinus.backend.repository.ThemeRepository;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;


    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeDto convertToDto(Theme theme) {
        return new ThemeDto(
                theme.getName(),
                theme.getTerminal()
        );
    }

    public Theme convertToEntity(ThemeDto themeDto) {
        Theme theme = new Theme();
        theme.setName(themeDto.getName());
        theme.setTerminal(themeDto.getTerminalTheme());
        return theme;
    }
}