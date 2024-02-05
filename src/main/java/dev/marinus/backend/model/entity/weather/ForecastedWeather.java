package dev.marinus.backend.model.entity.weather;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Table(name = "forecasted_weather")
@Entity(name = "forecasted_weather")
@Getter
public class ForecastedWeather {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Date date;
    private Date lastUpdate;
    @OneToOne()
    @JoinColumn(name = "forecasted_weather_data_id")
    private WeatherData weatherData;

}
