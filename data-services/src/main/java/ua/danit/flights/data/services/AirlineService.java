package ua.danit.flights.data.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ua.danit.flights.data.model.Airline;

/**
 * Service for operation of {@link Airline} objects
 * located in data store.
 *
 * @author Andrey Minov
 */
public interface AirlineService {
  /**
   * Gets information about airline by identifier.
   *
   * @param id unique airline identifier.
   * @return the airline found or empty is nothing was found.
   */
  Optional<Airline> getById(long id);

  /**
   * Gets information about airline by IATA code.
   *
   * @param iata the IATA 2 symbols code of airline
   * @return the airline found or empty is nothing was found.
   */
  List<Airline> getByIata(String iata);

  /**
   * Gets all airlines located in the storage.
   *
   * @return the all airlines located in the storage.
   */
  Collection<Airline> getAll();
}
