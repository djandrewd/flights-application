package ua.danit.flights.data.openflights.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.openflights.utils.ParseUtils;
import ua.danit.flights.data.services.AirlineService;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;

/**
 * Information about routes and flights by OpenFlights database.
 *
 * @author Andrey Minov
 */
public class OpenFlightsRoutesService implements RoutesService {
  private static final Path DEFAULT_PATH;

  static {
    try {
      DEFAULT_PATH =
          Paths.get(OpenFlightsAirportService.class.getResource("/routes.dat.csv").toURI());
    } catch (URISyntaxException e) {
      throw new Error(e);
    }
  }

  private Map<Airport, List<FlightRoute>> routes;
  private Map<Long, FlightRoute> routesById;

  /**
   * Instantiates a new Open flights routes service.
   *
   * @param airlineService the airline service
   * @param airportService the airport service
   */
  public OpenFlightsRoutesService(AirlineService airlineService, AirportService airportService) {
    this(airlineService, airportService, DEFAULT_PATH);
  }

  /**
   * Instantiates a new Open flights routes service.
   *
   * @param airlineService the airline service for getting information about airlines.
   * @param airportService the airport service for getting information about airports.
   * @param path           the path where file containing routes is located
   */
  public OpenFlightsRoutesService(AirlineService airlineService, AirportService airportService,
                                  Path path) {
    try {
      routes = new ConcurrentHashMap<>();
      routesById = new ConcurrentHashMap<>();
      Files.readAllLines(path, Charset.forName("UTF-8")).stream()
           .map(v -> ParseUtils.parseRoute(airlineService, airportService, v))
           .filter(Objects::nonNull).forEach(v -> {
             routesById.put(v.getId(), v);
             routes.computeIfAbsent(v.getDeparture(), k -> new ArrayList<>()).add(v);
           });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<FlightRoute> getById(long id) {
    return Optional.ofNullable(routesById.get(id));
  }

  @Override
  public List<FlightRoute> getDirectRoutes(Airport departure, Airport destination) {
    return getDirectRoutesFrom(departure).stream().filter(v -> destination.equals(v.getArrival()))
                                         .collect(Collectors.toList());
  }

  @Override
  public List<FlightRoute> getDirectRoutesFrom(Airport departure) {
    return Optional.ofNullable(routes.get(departure)).orElse(Collections.emptyList());
  }

  @Override
  public List<FlightRoute> getAll() {
    return new ArrayList<>(routesById.values());
  }
}
