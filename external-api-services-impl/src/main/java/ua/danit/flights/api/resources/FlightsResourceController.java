package ua.danit.flights.api.resources;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.danit.flights.api.model.Airline;
import ua.danit.flights.api.model.Airport;
import ua.danit.flights.api.model.FlightRoute;
import ua.danit.flights.api.model.FlightRoute.Flight;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.engine.FlightsEngine;

/**
 * REST resource controller for searching the flights.
 *
 * @author Andrey Minov
 */
@RestController
@RequestMapping("/flights")
public class FlightsResourceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(FlightsResourceController.class);

  private static final String MDC_KEY = "process";
  private static final String MDC_PREFIX = "FL_%s";
  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_MAX_ON_PAGE = 50;

  private final FlightsEngine flightsEngine;
  private final AirportService airportService;

  /**
   * Instantiates a new Flights resource controller.
   *
   * @param flightsEngine  the flights engine for searching the flights.
   * @param airportService the airport service for searching information about airports.
   */
  @Autowired
  public FlightsResourceController(FlightsEngine flightsEngine, AirportService airportService) {
    this.flightsEngine = flightsEngine;
    this.airportService = airportService;
  }

  /**
   * Search flights between two airport with max number of the stops on provided
   * date, page and max results. Selected flights are ordered by
   * least number of flights inside single route (eg by stops numbers) to show
   * direct connections first.
   *
   * @param date             the date on which flight is departed.
   * @param departureIata    the departure IATA of the airport.
   * @param arrivalIata      the arrival IATA of the airport.
   * @param maxStops         the max stops during the flight route.
   * @param pageNumber       the page number of the selection.
   * @param maxResultsOnPage the max results on page.
   * @return the flights between two airport with max number of the stops on provided
   *         date, page and max results.
   */
  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FlightRoute>> searchFlights(
      @RequestParam("date") @DateTimeFormat(iso = DATE) LocalDate date,
      @RequestParam("departure") String departureIata, @RequestParam("arrival") String arrivalIata,
      @RequestParam("stops") int maxStops, @RequestParam("page") Optional<Integer> pageNumber,
      @RequestParam("max_results") Optional<Integer> maxResultsOnPage) {
    MDC.put(MDC_KEY, String.format(MDC_PREFIX, UUID.randomUUID().toString()));
    try {
      LOGGER.debug("Receive search request for date [{}] from [{}] to [{}] with stop numbers {}.",
          date, departureIata, arrivalIata, maxStops);
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

      Collection<List<ua.danit.flights.data.model.Flight>> flights =
          flightsEngine.searchFlights(departure, arrival, date, maxStops, pageNum, pageSize);
      LOGGER.debug("Found {} flights matching the criteria on page {}.", flights.size(), pageNum);
      return ResponseEntity.ok(convertFlights(flights));
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
      Collection<List<ua.danit.flights.data.model.Flight>> flights) {
    List<FlightRoute> out = new ArrayList<>(flights.size());
    for (List<ua.danit.flights.data.model.Flight> travelRoute : flights) {
      List<Flight> transports = new ArrayList<>(travelRoute.size());
      for (ua.danit.flights.data.model.Flight flight : travelRoute) {
        ua.danit.flights.data.model.FlightRoute flightRoute = flight.getRoute();
        ua.danit.flights.data.model.Airport departure = flightRoute.getDeparture();
        ua.danit.flights.data.model.Airport arrival = flightRoute.getArrival();
        ua.danit.flights.data.model.Airline operator = flightRoute.getOperator();

        ZonedDateTime departureTime = null;
        if (flightRoute.getDepartureTime() != null) {
          departureTime = flight.getDate().atTime(flightRoute.getDepartureTime())
                                .atZone(ZoneId.of(departure.getTimeZone()));
        }

        ZonedDateTime arrivalTime = null;
        if (flightRoute.getDepartureTime() != null) {
          arrivalTime = flight.getDate().atTime(flightRoute.getArrivalTime())
                              .atZone(ZoneId.of(arrival.getTimeZone()));
          if (departureTime != null && departureTime.isAfter(arrivalTime)) {
            arrivalTime = arrivalTime.plusDays(1);
          }
        }

        Flight transport =
            new Flight(flight.getRoute().getName(), fromAirport(departure), fromAirport(arrival),
                fromAirline(operator), departureTime, arrivalTime);
        transports.add(transport);
      }

      out.add(new FlightRoute(transports, BigDecimal.ZERO));
    }
    return out;
  }
}
