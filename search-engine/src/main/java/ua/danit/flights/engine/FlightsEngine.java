package ua.danit.flights.engine;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.services.FlightsService;

/**
 * Engine for determination of flights between two airports for current date
 * using all availability criteria
 * (for example do not allow 24 hours for exchange between airlines).
 * <p/>
 * Use {@link RoutesEngine} to find routes and {@link FlightsService} to search flights for
 * concrete date.
 *
 * @author Andrey Minov
 */
public interface FlightsEngine {


  /**
   * Search for flights for provided date from departure to destination airport.
   *
   * @param departureAirport   the departure airport IATA code
   * @param destinationAirport the destination airport IATA airport
   * @param date               the date for the flight.
   * @param maxStops           max number of stops during search
   * @param maxResults         max results of the selection for given page.
   * @param pageNumber         number of the page to select counted from 0.
   * @return the list of flights available for client to register in.
   */
  Collection<List<Flight>> searchFlights(Airport departureAirport, Airport destinationAirport,
                                         LocalDate date, int maxStops, int pageNumber,
                                         int maxResults);
}
