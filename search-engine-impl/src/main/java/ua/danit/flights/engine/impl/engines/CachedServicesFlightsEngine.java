package ua.danit.flights.engine.impl.engines;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.FlightsService;
import ua.danit.flights.engine.RoutesEngine;
import ua.danit.flights.engine.ServicesFlightsEngine;

/**
 * Cached implementation of the services flights engine which uses cache
 * route engine implementation.
 *
 * @author Andrey Minov
 */
@Service
public class CachedServicesFlightsEngine extends ServicesFlightsEngine {

  @Autowired
  public CachedServicesFlightsEngine(FlightsService flightsService, RoutesEngine routesEngine) {
    super(flightsService, routesEngine);
  }

  @Override
  public Collection<List<Flight>> searchFlights(Airport departureAirport,
                                                Airport destinationAirport, LocalDate date,
                                                int maxStops, int pageNumber, int maxResults) {
    Collection<List<FlightRoute>> routes = getRoutesEngine()
        .findRoute(departureAirport, destinationAirport, maxStops, pageNumber, maxResults);
    if (routes.isEmpty()) {
      return Collections.emptyList();
    }

    Collection<List<Flight>> result = new ArrayList<>();
    for (List<FlightRoute> route : routes) {
      // Filter our routes available at the time of selection.
      if (!route.stream().allMatch(timesValid())) {
        continue;
      }

      // Time to track availability and date time for airport at airpoint timezone.
      List<Flight> flights = new ArrayList<>();
      LocalDate currentDate = date;
      for (FlightRoute move : route) {
        // Check that route is valid at current date.
        if (!timeValid(move, currentDate)) {
          flights.clear();
          break;
        }

        // Get flight and check if it is available at this time.
        Flight flight = getFlightsService().getOrCreateFlight(move, currentDate);
        if (flight.getAvailableSeats() == 0) {
          flights.clear();
          break;
        }

        // Add flight as possible candidate.
        flights.add(flight);

        // Hope planes does not flight longer then 24 hours :)
        if (move.getArrivalTime().isBefore(move.getDepartureTime())) {
          currentDate = currentDate.plusDays(1);
        }
      }

      // Check is flights are added.
      if (flights.isEmpty()) {
        continue;
      }

      result.add(flights);
    }
    return result;
  }

  private Predicate<FlightRoute> timesValid() {
    return r -> r.getDepartureTime() != null && r.getArrivalTime() != null;
  }

  private boolean timeValid(FlightRoute route, LocalDate date) {
    return (route.getStartDate() == null || !route.getStartDate().isAfter(date)) && (
        route.getEndDate() == null || !route.getEndDate().isBefore(date));
  }
}
