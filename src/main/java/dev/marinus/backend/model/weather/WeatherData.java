package dev.marinus.backend.model.weather;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private double temperature;
    private double windSpeed;
    private double windDirection;
    private double cloudCover;
    private double pressure;
    private double humidity;
}