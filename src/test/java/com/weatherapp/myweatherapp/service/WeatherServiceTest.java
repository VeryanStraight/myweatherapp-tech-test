package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

  @Mock
  private VisualcrossingRepository weatherRepo;

  @InjectMocks
  private WeatherService weatherService;

  private CityInfo sunnyCity;
  private CityInfo rainyCity;

  @BeforeEach
  public void setup() {
    sunnyCity = new CityInfo();
    CityInfo.CurrentConditions sunnyConditions = new CityInfo.CurrentConditions();
    sunnyConditions.setSunrise("06:00:00");
    sunnyConditions.setSunset("18:00:00");
    sunnyConditions.setConditions("Clear");
    sunnyCity.setCurrentConditions(sunnyConditions);
    sunnyCity.setAddress("Sunnyville");
    sunnyCity.setDescription("A sunny city");

    rainyCity = new CityInfo();
    CityInfo.CurrentConditions rainyConditions = new CityInfo.CurrentConditions();
    rainyConditions.setSunrise("07:00:00");
    rainyConditions.setSunset("19:05:00");
    rainyConditions.setConditions("Rain");
    rainyCity.setCurrentConditions(rainyConditions);
    rainyCity.setAddress("Rainyville");
    rainyCity.setDescription("A rainy city");
  }

  @Test
  public void testForecastByCity() {
    when(weatherRepo.getByCity("Sunnyville")).thenReturn(sunnyCity);

    CityInfo result = weatherService.forecastByCity("Sunnyville");

    assertEquals(sunnyCity, result);
  }

  @Test
  public void testIsRaining() {
    when(weatherRepo.getByCity("Rainyville")).thenReturn(rainyCity);

    boolean result = weatherService.isRaining("Rainyville");

    assertTrue(result);
  }

  @Test
  public void testIsNotRaining() {
    when(weatherRepo.getByCity("Sunnyville")).thenReturn(sunnyCity);
    boolean result = weatherService.isRaining("Sunnyville");
    assertFalse(result);
  }

  @Test
  public void testCompareDaylight() {
    int result = weatherService.compareDaylight(sunnyCity, rainyCity);
    assertEquals(-300, result);
  }
}
