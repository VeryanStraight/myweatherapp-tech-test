# MyWeather App Tech Test

## My Code


- WeatherController: added /compareDaylight and /rainCheck endpoints, both of which take two country names as parameters.
  compareDaylight returns the city weather info of the city with the longest day,
  or the first city weather info if they have the same length of day.
  rainCheck returns a map of boolean values for if the city is raining.
  e.g if given London (raining) and Wellington (not raining) it returns {"London": true, "Wellington": false}
  The brief was vague on what was necessary for the rainCheck endpoint
  as it merely said "check which city it is currently raining in."
  So, as raining is not mutually exclusive, I returned the status of both cities.  
- CityInfo:
  I added getters and setters with Lombok
  and made the inner classes public instead of package private
  so the service could access parameters such as sunrise and sunset.
- WeatherService: added several methods to manage the logic of the new endpoints (e.g. getDaylightDuration)
- WeatherServiceTest: added tests for the service layer. Used mockito to mock the VisualcrossingRepository.
- WeatherControllerTest: added tests for the controller layer. Used mockito to mock the service layer.

Note: you will need to replace API_KEY_PLACEHOLDER in application.properties with your own key.