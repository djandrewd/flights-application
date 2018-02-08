package ua.danit.flights.data.openflights.utils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import ua.danit.flights.data.model.Airline;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.openflights.model.OpenFlightsAirline;
import ua.danit.flights.data.openflights.model.OpenFlightsAirport;
import ua.danit.flights.data.openflights.model.OpenFlightsFlightRoute;
import ua.danit.flights.data.services.AirlineService;
import ua.danit.flights.data.services.AirportService;

/**
 * Parse utilities for open flights CSV format.
 *
 * @author Andrey Minov
 */
public final class ParseUtils {

  private static final String EMPTY = "\\N";
  private static final AtomicLong ROUTE_ID_COUNTER = new AtomicLong(0);

  /**
   * Parse line of CSV file into {@link Airport} value.
   *
   * @param line the line
   * @return parsed {@link Airport} value.
   * @throws IllegalArgumentException in case provided line incorrect.
   */
  public static Airport parseAirport(String line) {
    CsvScanner scanner = new CsvScanner(line);
    try {
      OpenFlightsAirport airport = new OpenFlightsAirport();
      airport.setId(scanner.nextLong());
      airport.setName(scanner.nextString());
      airport.setCity(scanner.nextString());
      airport.setCountry(scanner.nextString());
      airport.setIataCode(scanner.nextString());
      airport.setIcaoCode(scanner.nextString());
      airport.setLatitude(scanner.nextDouble());
      airport.setLongitude(scanner.nextDouble());
      scanner.nextLong();
      scanner.nextLong();
      scanner.nextString();
      airport.setTimeZone(scanner.nextString());
      return airport;
    } catch (Exception e) {
      throw new IllegalArgumentException("Line is not parsed: " + line + ".", e);
    }
  }

  /**
   * Parse line of CSV file into {@link Airline} value.
   *
   * @param line the line
   * @return parsed {@link Airline} value.
   * @throws IllegalArgumentException in case provided line incorrect.
   */
  public static Airline parseAirline(String line) {
    CsvScanner scanner = new CsvScanner(line);
    try {
      OpenFlightsAirline airline = new OpenFlightsAirline();
      airline.setId(scanner.nextLong());
      airline.setName(scanner.nextString());
      scanner.nextString();
      airline.setIata(scanner.nextString());
      airline.setIcao(scanner.nextString());
      scanner.nextString();
      airline.setCountry(scanner.nextString());
      return airline;
    } catch (Exception e) {
      throw new IllegalArgumentException("Line is not parsed: " + line + ".", e);
    }
  }

  /**
   * Parse line of CSV file into {@link FlightRoute} value.
   *
   * @param airlineService the airline service
   * @param airportService the airport service
   * @param line           the line
   * @return parsed {@link FlightRoute} value.
   * @throws IllegalArgumentException in case provided line incorrect.
   */
  public static FlightRoute parseRoute(AirlineService airlineService, AirportService airportService,
                                       String line) {
    try {
      String[] parts = line.split(",");
      if (EMPTY.equals(parts[1]) || EMPTY.equals(parts[3]) || EMPTY.equals(parts[5])) {
        return null;
      }

      OpenFlightsFlightRoute route = new OpenFlightsFlightRoute();
      route.setId(ROUTE_ID_COUNTER.incrementAndGet());
      long id = Long.parseLong(parts[1]);
      Optional<Airline> operator = airlineService.getById(id);
      if (!operator.isPresent()) {
        return null;
      }

      route.setOperator(operator.get());
      id = Long.parseLong(parts[3]);
      Optional<Airport> departure = airportService.getById(id);
      if (!departure.isPresent()) {
        return null;
      }

      id = Long.parseLong(parts[5]);
      route.setDeparture(departure.get());
      Optional<Airport> arrival = airportService.getById(id);
      if (!arrival.isPresent()) {
        return null;
      }
      route.setArrival(arrival.get());

      String share = parts[6];
      if ("Y".equals(share)) {
        return null;
      }
      return route;
    } catch (Exception e) {
      throw new IllegalArgumentException("Line is not parsed: " + line + ".", e);
    }
  }
}
