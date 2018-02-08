package ua.danit.flights.api.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.danit.flights.api.model.Airport;
import ua.danit.flights.data.services.AirportService;

/**
 * Resource controller responsible for searching airports.
 *
 * @author Andrey Minov
 */
@RestController
@RequestMapping("/airports")
public class AirportsResourceController {

  private final AirportService airportService;

  /**
   * Instantiates a new Airports resource controller.
   *
   * @param airportService the airport service for proving information about airport.
   */
  @Autowired
  public AirportsResourceController(AirportService airportService) {
    this.airportService = airportService;
  }

  /**
   * Find airports by city provided from user. City name will searches by
   * containing not by equals.
   *
   * @param city the city part used for searches.
   * @return the list of airport found by provided city or empty if nothing was found.
   */
  @GetMapping(value = "/byCity", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Airport> findAirportByCity(@RequestParam String city) {
    return airportService.findByCity(city).stream().map(
        a -> new Airport(a.getIata(), a.getName(), a.getCity(), a.getCountry(), a.getTimeZone()))
                         .collect(Collectors.toList());
  }

  /**
   * Find airport by IATA code. You can specify any number if letter till 3 for find
   * valid list of airports.
   *
   * @param iata the IATA code of the airport to search.
   * @return the list of airport found by provided city or empty if nothing was found.
   */
  @GetMapping(value = "/byIata", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Airport> findAirportByIata(@RequestParam String iata) {
    return airportService.findByIata(iata).stream().map(
        a -> new Airport(a.getIata(), a.getName(), a.getCity(), a.getCountry(), a.getTimeZone()))
                         .collect(Collectors.toList());
  }
}
