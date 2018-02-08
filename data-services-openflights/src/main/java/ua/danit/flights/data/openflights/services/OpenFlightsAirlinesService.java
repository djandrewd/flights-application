package ua.danit.flights.data.openflights.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import ua.danit.flights.data.model.Airline;
import ua.danit.flights.data.openflights.utils.ParseUtils;
import ua.danit.flights.data.services.AirlineService;

/**
 * Airlines service using OpenFlights data.
 *
 * @author Andrey Minov
 */
public class OpenFlightsAirlinesService implements AirlineService {
  private static final Path DEFAULT_PATH;

  static {
    try {
      DEFAULT_PATH =
          Paths.get(OpenFlightsAirportService.class.getResource("/airlines.dat.csv").toURI());
    } catch (URISyntaxException e) {
      throw new Error(e);
    }
  }

  private Map<String, List<Airline>> airlines;
  private Map<Long, Airline> airlinesById;

  /**
   * Instantiates a new Open flights airlines service.
   */
  public OpenFlightsAirlinesService() {
    this(DEFAULT_PATH);
  }

  /**
   * Instantiates a new Open flights airlines service.
   *
   * @param path the path to file where open flights information about airlines located.
   */
  public OpenFlightsAirlinesService(Path path) {
    try {
      airlinesById = new ConcurrentHashMap<>();
      airlines = new ConcurrentHashMap<>();
      Files.readAllLines(path, Charset.forName("UTF-8")).stream().map(ParseUtils::parseAirline)
           .filter(v -> Objects.nonNull(v.getIata())).filter(v -> v.getIata().length() == 2)
           .forEach(v -> {
             airlinesById.put(v.getId(), v);
             airlines.computeIfAbsent(v.getIata(), k -> new ArrayList<>()).add(v);
           });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Airline> getById(long id) {
    return Optional.ofNullable(airlinesById.get(id));
  }

  @Override
  public List<Airline> getByIata(String iata) {
    return Optional.ofNullable(airlines.get(iata)).orElse(Collections.emptyList());
  }

  @Override
  public Collection<Airline> getAll() {
    return airlinesById.values();
  }
}
