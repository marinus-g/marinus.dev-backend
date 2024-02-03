package dev.marinus.backend.controller;

import dev.marinus.backend.dto.WeatherDto;
import dev.marinus.backend.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(name = "weather")
    public ResponseEntity<WeatherDto> getWeather(@RequestParam String city) {
        return ResponseEntity.ok(new WeatherDto());
    }
}