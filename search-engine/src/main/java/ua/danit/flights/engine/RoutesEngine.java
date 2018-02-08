package ua.danit.flights.engine;

import java.util.Collection;
import java.util.List;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;

/**
 * Engine for determination of route between two airports.
 * It uses {@link ua.danit.flights.data.services.AirportService}
 * for loading information about airports
 * and {@link ua.danit.flights.data.services.RoutesService} to find out direct connections.
 *
 * @author Andrey Minov
 */
public interface RoutesEngine {

  /**
   * Find routes between two airport using maximum number of steps.
   *
   * @param departure  the departure airport
   * @param arrival    the destination airport
   * @param maxStops   the max stops during the route
   * @param maxResults max results of the selection for given page.
   * @param pageNumber number of the page to select counted from 0.
   * @return the list of routes between two points with provided number of steps.
   */
  Collection<List<FlightRoute>> findRoute(Airport departure, Airport arrival, int maxStops,
                                          int pageNumber, int maxResults);

  /**
   * Instantiate current Route engine.
   */
  default void instantiate() {
  }
}
