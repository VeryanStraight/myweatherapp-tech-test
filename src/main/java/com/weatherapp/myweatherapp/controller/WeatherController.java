package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for handling weather-related HTTP requests.
 * Provides endpoints for fetching weather forecasts, comparing daylight duration,
 * and checking for rain in specified cities.
 */
@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  /**
   * Handles GET requests to fetch weather forecast for a given city.
   * @param city - the name of the city
   * @return ResponseEntity<CityInfo> - the weather information of the specified city
   */
  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {
    CityInfo ci = weatherService.forecastByCity(city);
    return ResponseEntity.ok(ci);
  }

  /**
   * Handles GET requests to compare daylight duration between two cities.
   * @param city1 - the name of the first city
   * @param city2 - the name of the second city
   * @return ResponseEntity<CityInfo> - the city with the longer daylight duration
   */
  @GetMapping("/daylight-comparison")
  public ResponseEntity<CityInfo> compareDaylight(@RequestParam("city1") String city1, @RequestParam("city2") String city2) {
    try {
      CityInfo cityInfo1 = weatherService.forecastByCity(city1);
      CityInfo cityInfo2 = weatherService.forecastByCity(city2);

      CityInfo longerDayCity = weatherService.compareDaylight(cityInfo1, cityInfo2) >= 0 ? cityInfo1 : cityInfo2;
      return ResponseEntity.ok(longerDayCity);
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).build();
    }
  }

  /**
   * Handles GET requests to check if it is raining in two cities.
   * @param city1 - the name of the first city
   * @param city2 - the name of the second city
   * @return ResponseEntity<Map<String, Boolean>> - a JSON object mapping each city to its rain status
   */
  @GetMapping("/rain-check")
  public ResponseEntity<Map<String, Boolean>> rainCheck(@RequestParam("city1") String city1, @RequestParam("city2") String city2) {
    try {
      boolean isRainingCity1 = weatherService.isRaining(city1);
      boolean isRainingCity2 = weatherService.isRaining(city2);

      Map<String, Boolean> response = new HashMap<>();
      response.put(city1, isRainingCity1);
      response.put(city2, isRainingCity2);
      return ResponseEntity.ok(response);
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).build();
    }
  }
}
