package ua.danit.flights.api.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.danit.flights.api.model.Airline;
import ua.danit.flights.api.model.Airport;
import ua.danit.flights.api.model.FlightRoute;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.engine.RoutesEngine;

/**
 * This is resource controller for route flights between two airports.
 * This resource might give you flights schedule.
 *
 * @author Andrey Minov
 */
@RestController
@RequestMapping("/routes")
public class RoutesResourceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(FlightsResourceController.class);

  private static final String MDC_KEY = "process";
  private static final String MDC_PREFIX = "RR_%s";
  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_MAX_ON_PAGE = 50;

  private final RoutesEngine routesEngine;
  private final AirportService airportService;

  /**
   * Instantiates a new Routes resource controller.
   *
   * @param routesEngine   the routes engine
   * @param airportService the airport service
   */
  @Autowired
  public RoutesResourceController(RoutesEngine routesEngine, AirportService airportService) {
    this.routesEngine = routesEngine;
    this.airportService = airportService;
  }

  /**
   * Search routes between two airport with max number of the stops,
   * page and max results. Selected routes are ordered by
   * least number of flights inside single route (eg by stops numbers) to show
   * direct connections first.
   *
   * @param departureIata    the departure IATA of the airport.
   * @param arrivalIata      the arrival IATA of the airport.
   * @param maxStops         the max stops during the flight route.
   * @param pageNumber       the page number of the selection.
   * @param maxResultsOnPage the max results on page.
   * @return the routes between two airport with max number of the stops on provided
   *         date, page and max results.
   */

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FlightRoute>> searchRoutes(
      @RequestParam("departure") String departureIata, @RequestParam("arrival") String arrivalIata,
      @RequestParam("stops") int maxStops, @RequestParam("page") Optional<Integer> pageNumber,
      @RequestParam("max_results") Optional<Integer> maxResultsOnPage) {
    MDC.put(MDC_KEY, String.format(MDC_PREFIX, UUID.randomUUID().toString()));
    try {
      LOGGER.debug("Receive routes search request from [{}] to [{}] with stop numbers {}.",
          departureIata, arrivalIata, maxStops);
      List<ua.danit.flights.data.model.Airport> opt = airportService.getByIata(departureIata);
      if (opt.isEmpty()) {
        LOGGER.info("Airport with IATA {} do not presented in the system!", departureIata);
        return ResponseEntity.badRequest().build();
      }
      ua.danit.flights.data.model.Airport departure = opt.get(0);
      opt = airportService.getByIata(arrivalIata);
      if (opt.isEmpty()) {
        LOGGER.info("Airport with IATA {} do not presented in the system!", arrivalIata);
        return ResponseEntity.badRequest().build();
      }
      ua.danit.flights.data.model.Airport arrival = opt.get(0);
      int pageNum = pageNumber.orElse(DEFAULT_PAGE);
      int pageSize = maxResultsOnPage.orElse(DEFAULT_MAX_ON_PAGE);

      Collection<List<ua.danit.flights.data.model.FlightRoute>> routes =
          routesEngine.findRoute(departure, arrival, maxStops, pageNum, pageSize);
      LOGGER.debug("Found {} routes matching the criteria on page {}.", routes.size(), pageNum);
      return ResponseEntity.ok(convertFlights(routes));
    } finally {
      MDC.remove(MDC_KEY);
    }
  }

  private Airport fromAirport(ua.danit.flights.data.model.Airport airport) {
    return new Airport(airport.getIata(), airport.getName(), airport.getCity(),
        airport.getCountry(), airport.getTimeZone());
  }

  private Airline fromAirline(ua.danit.flights.data.model.Airline airline) {
    return new Airline(airline.getName(), airline.getIata(), airline.getCountry());
  }

  private List<FlightRoute> convertFlights(
      Collection<List<ua.danit.flights.data.model.FlightRoute>> flights) {
    List<FlightRoute> out = new ArrayList<>(flights.size());
    for (List<ua.danit.flights.data.model.FlightRoute> travelRoute : flights) {
      List<FlightRoute.Flight> transports = new ArrayList<>(travelRoute.size());
      for (ua.danit.flights.data.model.FlightRoute flightRoute : travelRoute) {
        ua.danit.flights.data.model.Airport departure = flightRoute.getDeparture();
        ua.danit.flights.data.model.Airport arrival = flightRoute.getArrival();
        ua.danit.flights.data.model.Airline operator = flightRoute.getOperator();

        FlightRoute.Flight transport =
            new FlightRoute.Flight(flightRoute.getName(), fromAirport(departure),
                fromAirport(arrival),
                fromAirline(operator), null, null);
        transports.add(transport);
      }

      out.add(new FlightRoute(transports, null));
    }
    return out;
  }
}
