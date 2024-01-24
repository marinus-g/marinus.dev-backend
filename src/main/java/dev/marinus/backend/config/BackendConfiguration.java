package dev.marinus.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BackendConfiguration {

    @Value("${weather.api.key}")
    private String weatherApiKey;
}