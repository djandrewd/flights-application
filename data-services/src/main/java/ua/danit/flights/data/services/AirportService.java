package ua.danit.flights.data.services;

import java.util.List;
import java.util.Optional;

import ua.danit.flights.data.model.Airport;

/**
 * Service for operation of {@link Airport} objects
 * located in data store.
 *
 * @author Andrey Minov
 */
public interface AirportService {
  /**
   * Gets airport instance by id.
   *
   * @param id the unique airport identifier.
   * @return the airport found or empty is nothing was found.
   */
  Optional<Airport> getById(long id);

  /**
   * Gets information about airport by IATA code.
   *
   * @param iata the IATA 3 symbols code of airpot
   * @return the airport found or empty is nothing was found.
   */
  List<Airport> getByIata(String iata);


  /**
   * Gets all airports located in the storage.
   *
   * @return the all airports located in the storage.
   */
  List<Airport> getAll();

  /**
   * Find list of airports matching provided city name substring.
   *
   * @param city part of city provided for search.
   * @return list of all airports with city partially match current city.
   */
  List<Airport> findByCity(String city);

  /**
   * Gets information about airport by partial IATA code.
   *
   * @param iata the IATA 1 of 3 symbols code of airport
   * @return the airports found or empty is nothing was found.
   */
  List<Airport> findByIata(String iata);
}
