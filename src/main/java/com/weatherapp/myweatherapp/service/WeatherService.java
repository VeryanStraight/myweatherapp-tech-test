package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling weather-related operations.
 * Provides methods to fetch weather data, check for rain,
 * and compare daylight durations between cities.
 */
@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  /**
   * Fetches the weather forecast for a given city.
   * @param city - the name of the city
   * @return CityInfo - the weather information of the specified city
   */
  public CityInfo forecastByCity(String city) {
    return weatherRepo.getByCity(city);
  }

  /**
   * Checks if it is currently raining in the specified city.
   * @param city - the name of the city
   * @return boolean - true if it is raining, false otherwise
   */
  public boolean isRaining(String city) {
    CityInfo cityInfo = forecastByCity(city);
    return cityInfo.getCurrentConditions().getConditions().toLowerCase().contains("rain");
  }

  /**
   * Compares the daylight duration between two cities.
   * @param cityOne - the first city
   * @param cityTwo - the second city
   * @return int - the difference of daylight duration in seconds
   */
  public int compareDaylight(CityInfo cityOne, CityInfo cityTwo) {
    int daylightOne = getDaylightDuration(cityOne);
    int daylightTwo = getDaylightDuration(cityTwo);
    return daylightOne - daylightTwo;
  }

  /**
   * Calculates the daylight duration of a city based on sunrise and sunset times.
   * @param city - the city
   * @return int - the daylight duration in seconds
   */
  private int getDaylightDuration(CityInfo city) {
    String sunrise = city.getCurrentConditions().getSunrise();
    String sunset = city.getCurrentConditions().getSunset();

    int sunriseSeconds = convertToSeconds(sunrise);
    int sunsetSeconds = convertToSeconds(sunset);

    return sunsetSeconds - sunriseSeconds;
  }

  /**
   * Converts time from HH:mm:ss format to total seconds.
   * @param time - the time in HH:mm:ss format
   * @return int - the time in seconds
   */
  private int convertToSeconds(String time) {
    String[] parts = time.split(":");
    int hours = Integer.parseInt(parts[0]);
    int minutes = Integer.parseInt(parts[1]);
    int seconds = Integer.parseInt(parts[2]);
    return hours * 3600 + minutes * 60 + seconds;
  }
}