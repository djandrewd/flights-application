package ua.danit.flights.data.services;

import java.util.List;
import java.util.Optional;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;

/**
 * Service for operation of {@link FlightRoute} objects
 * located in data store.
 *
 * @author Andrey Minov
 */
public interface RoutesService {

  /**
   * Gets route instance by identity.
   *
   * @param id the unique identity of the route
   * @return the found route or empty is nothing is found.
   */
  Optional<FlightRoute> getById(long id);

  /**
   * Gets all direct routes connected two airports.
   *
   * @param departure   the departure airport.
   * @param destination the destination airport.
   * @return the direct routes connected two airports or empty list is not direct route exists.
   */
  List<FlightRoute> getDirectRoutes(Airport departure, Airport destination);

  /**
   * Get all direct routes departed from departure airport.
   *
   * @param departure airport of departure.
   * @return return list of routes departed from departure airport.
   */
  List<FlightRoute> getDirectRoutesFrom(Airport departure);

  /*
  * Get all flight routes existed in the data storage.
  *
  * @return list of flight routes available in the system.
  * **/
  List<FlightRoute> getAll();
}
