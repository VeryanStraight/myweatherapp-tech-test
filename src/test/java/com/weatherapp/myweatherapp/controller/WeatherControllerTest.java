package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private CityInfo sunnyCity;
    private CityInfo rainyCity;

    // Setup method to initialize mockMvc and mock data
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();

        // Setup sunnyCity
        sunnyCity = new CityInfo();
        CityInfo.CurrentConditions sunnyConditions = new CityInfo.CurrentConditions();
        sunnyConditions.setSunrise("06:00:00");
        sunnyConditions.setSunset("18:00:00");
        sunnyConditions.setConditions("Clear");
        sunnyCity.setCurrentConditions(sunnyConditions);
        sunnyCity.setAddress("Sunnyville");
        sunnyCity.setDescription("A sunny city");

        // Setup rainyCity
        rainyCity = new CityInfo();
        CityInfo.CurrentConditions rainyConditions = new CityInfo.CurrentConditions();
        rainyConditions.setSunrise("07:00:00");
        rainyConditions.setSunset("19:00:00");
        rainyConditions.setConditions("Rain");
        rainyCity.setCurrentConditions(rainyConditions);
        rainyCity.setAddress("Rainyville");
        rainyCity.setDescription("A rainy city");
    }

    // Test for comparing daylight between two cities
    @Test
    public void testCompareDaylight() throws Exception {
        when(weatherService.forecastByCity("Sunnyville")).thenReturn(sunnyCity);
        when(weatherService.forecastByCity("Rainyville")).thenReturn(rainyCity);
        when(weatherService.compareDaylight(sunnyCity, rainyCity)).thenReturn(-3600); // Sunnyville has shorter daylight

        mockMvc.perform(get("/compareDaylight")
                        .param("city1", "Sunnyville")
                        .param("city2", "Rainyville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Rainyville"))
                .andExpect(jsonPath("$.description").value("A rainy city"));
    }

    // Test for rain check between two cities
    @Test
    public void testRainCheck() throws Exception {
        when(weatherService.isRaining("Sunnyville")).thenReturn(false);
        when(weatherService.isRaining("Rainyville")).thenReturn(true);

        mockMvc.perform(get("/rainCheck")
                        .param("city1", "Sunnyville")
                        .param("city2", "Rainyville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(false))
                .andExpect(jsonPath("$[1]").value(true));
    }

    // Test for invalid city input in compareDaylight
    @Test
    public void testCompareDaylight_InvalidCity() throws Exception {
        when(weatherService.forecastByCity("Sunnyville")).thenReturn(sunnyCity);
        when(weatherService.forecastByCity("InvalidCity")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/compareDaylight")
                        .param("city1", "Sunnyville")
                        .param("city2", "InvalidCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test for invalid city input in rainCheck
    @Test
    public void testRainCheck_InvalidCity() throws Exception {
        when(weatherService.isRaining("InvalidCity")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/rainCheck")
                        .param("city1", "InvalidCity")
                        .param("city2", "Rainyville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test for both cities raining in rainCheck
    @Test
    public void testRainCheck_BothCitiesRaining() throws Exception {
        when(weatherService.isRaining("Sunnyville")).thenReturn(true);
        when(weatherService.isRaining("Rainyville")).thenReturn(true);

        mockMvc.perform(get("/rainCheck")
                        .param("city1", "Sunnyville")
                        .param("city2", "Rainyville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(true))
                .andExpect(jsonPath("$[1]").value(true));
    }

    // Test for no cities raining in rainCheck
    @Test
    public void testRainCheck_NoCitiesRaining() throws Exception {
        when(weatherService.isRaining("Sunnyville")).thenReturn(false);
        when(weatherService.isRaining("Rainyville")).thenReturn(false);

        mockMvc.perform(get("/rainCheck")
                        .param("city1", "Sunnyville")
                        .param("city2", "Rainyville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(false))
                .andExpect(jsonPath("$[1]").value(false));
    }

    // Test for comparing daylight with equal day length
    @Test
    public void testCompareDaylight_SameDayLength() throws Exception {
        CityInfo cityWithSameDayLength = new CityInfo();
        CityInfo.CurrentConditions sameConditions = new CityInfo.CurrentConditions();
        sameConditions.setSunrise("06:00:00");
        sameConditions.setSunset("18:00:00");
        sameConditions.setConditions("Clear");
        cityWithSameDayLength.setCurrentConditions(sameConditions);
        cityWithSameDayLength.setAddress("Equalville");
        cityWithSameDayLength.setDescription("A city with equal day length");

        when(weatherService.forecastByCity("Sunnyville")).thenReturn(sunnyCity);
        when(weatherService.forecastByCity("Equalville")).thenReturn(cityWithSameDayLength);
        when(weatherService.compareDaylight(sunnyCity, cityWithSameDayLength)).thenReturn(0); // Same daylight duration

        mockMvc.perform(get("/compareDaylight")
                        .param("city1", "Sunnyville")
                        .param("city2", "Equalville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Sunnyville"))
                .andExpect(jsonPath("$.description").value("A sunny city"));
    }

    // Test for reverse order of cities in compareDaylight
    @Test
    public void testCompareDaylight_ReverseOrder() throws Exception {
        when(weatherService.forecastByCity("Rainyville")).thenReturn(rainyCity);
        when(weatherService.forecastByCity("Sunnyville")).thenReturn(sunnyCity);
        when(weatherService.compareDaylight(rainyCity, sunnyCity)).thenReturn(3600); // Rainyville has longer daylight

        mockMvc.perform(get("/compareDaylight")
                        .param("city1", "Rainyville")
                        .param("city2", "Sunnyville")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Rainyville"))
                .andExpect(jsonPath("$.description").value("A rainy city"));
    }
}
