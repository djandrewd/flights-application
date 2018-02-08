package ua.danit.flights.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;

/**
 * Route engine used services for manipulating the data.
 *
 * @author Andrey Minov
 */
public class ServicesRoutesEngine implements RoutesEngine {
  private RoutesService routesService;
  private AirportService airportService;

  public ServicesRoutesEngine(RoutesService routesService, AirportService airportService) {
    this.routesService = routesService;
    this.airportService = airportService;
  }

  @Override
  public Collection<List<FlightRoute>> findRoute(Airport departure, Airport arrival,
                                                 int maxStops, int pageNumber, int maxResults) {
    return Collections.emptyList();
  }

  protected RoutesService getRoutesService() {
    return routesService;
  }

  protected AirportService getAirportService() {
    return airportService;
  }
}
