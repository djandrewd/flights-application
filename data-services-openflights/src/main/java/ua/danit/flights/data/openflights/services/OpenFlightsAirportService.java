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
import ua.danit.flights.data.openflights.utils.ParseUtils;
import ua.danit.flights.data.services.AirportService;

/**
 * Open flights information about available airports.
 *
 * @author Andrey Minov.
 */
public class OpenFlightsAirportService implements AirportService {
  private static final Path DEFAULT_PATH;

  static {
    try {
      DEFAULT_PATH =
          Paths.get(OpenFlightsAirportService.class.getResource("/airports.dat.csv").toURI());

    } catch (URISyntaxException e) {
      throw new Error(e);
    }
  }

  private Map<String, List<Airport>> airports;
  private Map<Long, Airport> airportsById;

  /**
   * Instantiates a new Open flights airport service.
   */
  public OpenFlightsAirportService() {
    this(DEFAULT_PATH);
  }

  /**
   * Instantiates a new Open flights airport service.
   *
   * @param path the path to file where information about airports are localed.
   */
  public OpenFlightsAirportService(Path path) {
    try {
      airportsById = new ConcurrentHashMap<>();
      airports = new ConcurrentHashMap<>();
      Files.readAllLines(path, Charset.forName("UTF-8")).stream().map(ParseUtils::parseAirport)
           .filter(v -> Objects.nonNull(v.getIata())).forEach(v -> {
             airportsById.put(v.getId(), v);
             airports.computeIfAbsent(v.getIata(), k -> new ArrayList<>()).add(v);
           });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Airport> getById(long id) {
    return Optional.ofNullable(airportsById.get(id));
  }

  @Override
  public List<Airport> getByIata(String iata) {
    return Optional.ofNullable(airports.get(iata)).orElse(Collections.emptyList());
  }

  @Override
  public List<Airport> getAll() {
    return new ArrayList<>(airportsById.values());
  }

  @Override
  public List<Airport> findByCity(String city) {
    return airportsById.values().stream().filter(v -> v.getCity().contains(city))
                       .collect(Collectors.toList());
  }

  @Override
  public List<Airport> findByIata(String iata) {
    return airportsById.values().stream().filter(v -> v.getIata().contains(iata))
                       .collect(Collectors.toList());
  }
}
