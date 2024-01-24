package dev.marinus.backend.model.weather;

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
    @OneToOne(mappedBy = "forecasted_weather")
    private WeatherData weatherData;

}
