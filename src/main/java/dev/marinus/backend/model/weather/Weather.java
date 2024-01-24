package dev.marinus.backend.model.weather;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Table(name = "weather")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    private String city;

    @OneToOne(mappedBy = "weather")
    private WeatherData current;
    private Date lastUpdate;

    @OneToMany(mappedBy = "weather")
    private List<ForecastedWeather> forecast;

   public void addForecast(ForecastedWeather weather) {
        forecast.add(weather);
   }
}