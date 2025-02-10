# MyWeather App Tech Test

## My Code

I have modified/added:
- WeatherController: added /compareDaylight and /rainCheck endpoints, both of which take two country names as parameters. compareDaylight returns the city weather info of the city with the longest day, or the first city weather info if they have the same length of day. rainCheck returns a list of if the country is raining in the same order as the cities are given. the brief was vauge on what was necessary for the rainCheck end pint as it mearly "check which city it is currently raining in.", so as raining is not mutually exclusive, I returned the status of both cities.  
- CityInfo: I added getters and setters with lombok and made the inner classes public instead of package private so the service could access the parameters such as sunrise and sunset.
- WeatherService: added several methods to manage the logic of the new endpoints (e.g getDaylightDuration)
- WeatherServiceTest: added tests for the service layer. used mockito to mock the VisualcrossingRepository.
- WeatherControllerTest: added tests for the controller layer. used mockito to mock the service layer.




