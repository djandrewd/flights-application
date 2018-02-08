package ua.danit.flights.data.services;

import java.time.LocalDate;

import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.model.SeatClass;
import ua.danit.flights.data.model.User;

/**
 * Service for data storage used to search, store and remove {@link FlightsService} objects.
 *
 * @author Andrey Minov
 */
public interface FlightsService {

  /**
   * Gets or create new {@link FlightsService} objects for current date.
   *
   * @param route the route which holds all information about flight.
   * @param date  the date when this flight is operated.
   * @return the or create instance of flight for current date.
   */
  Flight getOrCreateFlight(FlightRoute route, LocalDate date);

  /**
   * Register seat on plane for flight for user on provided row and line.
   *
   * @param user   the user that register the seat.
   * @param flight the flight on which seat will be registered.
   * @param clazz  the clazz of the seat.
   * @return true in case seat is successfully registered, false otherwise.
   */
  boolean registerSeat(User user, Flight flight, SeatClass clazz);
}
