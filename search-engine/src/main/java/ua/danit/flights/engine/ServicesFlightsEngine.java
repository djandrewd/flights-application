package ua.danit.flights.engine;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.services.FlightsService;

/**
 * Flights service with uses services for deternimation of the flight route.
 *
 * @author Andrey Minov.
 */
public class ServicesFlightsEngine implements FlightsEngine {

  private FlightsService flightsService;
  private RoutesEngine routesEngine;

  public ServicesFlightsEngine(FlightsService flightsService, RoutesEngine routesEngine) {
    this.flightsService = flightsService;
    this.routesEngine = routesEngine;
  }

  @Override
  public Collection<List<Flight>> searchFlights(Airport departureAirport,
                                                Airport destinationAirport, LocalDate date,
                                                int maxStops, int pageNumber, int maxResults) {
    return Collections.emptyList();
  }

  protected FlightsService getFlightsService() {
    return flightsService;
  }

  protected RoutesEngine getRoutesEngine() {
    return routesEngine;
  }
}
