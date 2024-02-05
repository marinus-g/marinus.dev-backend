package dev.marinus.backend.service;

import dev.marinus.backend.config.BackendConfiguration;
import dev.marinus.backend.model.entity.weather.Weather;
import dev.marinus.backend.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.Date;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final BackendConfiguration backendConfiguration;

    public WeatherService(WeatherRepository weatherRepository,
                          BackendConfiguration backendConfiguration) {
        this.weatherRepository = weatherRepository;
        this.backendConfiguration = backendConfiguration;
    }

    public Optional<Weather> getWeatherInCity(String city) {
        Optional<Weather> weather =
                this.weatherRepository.findByCity(city).or(() -> fetchWeather(city));
        weather.ifPresent(this::updateWeatherIfNeeded);
        return weather;
    }

    private void updateWeatherIfNeeded(Weather weather) {
        Optional<Weather> updatedWeather = Optional.empty();
        if (weather.getLastUpdate().after(new Date(System.currentTimeMillis() - 1000 * 60 * 30))) {
            updatedWeather = fetchWeather(weather.getCity());
            updatedWeather.ifPresent(weather1 -> weather.setCurrent(weather1.getCurrent()));
        }
        if (weather.getForecast().stream().anyMatch(forecastedWeather -> forecastedWeather.getDate().after(new Date(System.currentTimeMillis() - 1000 * 60 * 30)))) {
            if (updatedWeather.isEmpty()) {
                updatedWeather = fetchWeather(weather.getCity());
            }
            updatedWeather.ifPresent(weather1 -> weather.setForecast(weather1.getForecast()));
        }
    }

    private Optional<Weather> fetchWeather(String city) {
        final String url = ("https://api.weatherapi.com/v1/forecast.json" +
                "?key=%s" +
                "&q=%s" +
                "&days=3" +
                "&aqi=no" +
                "&alerts=no").formatted(backendConfiguration.getWeatherApiKey(), city);
        HttpClient client = HttpClient.newHttpClient();
        //client.send(HttpURLConnection.(url), HttpResponse.BodyHandlers.ofString()
        return Optional.empty();
    }
}
