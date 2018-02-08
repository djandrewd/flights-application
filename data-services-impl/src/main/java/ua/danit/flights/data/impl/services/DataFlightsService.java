package ua.danit.flights.data.impl.services;

import java.time.LocalDate;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.danit.flights.data.impl.dao.FlightsDao;
import ua.danit.flights.data.impl.model.MappedFlight;
import ua.danit.flights.data.impl.model.MappedFlightRoute;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.model.Plane;
import ua.danit.flights.data.model.SeatClass;
import ua.danit.flights.data.model.User;
import ua.danit.flights.data.services.FlightsService;

/**
 * Data service for {@link ua.danit.flights.data.model.Flight} using data access object from
 * relational database.
 *
 * @author Andrey Minov
 */
@Service
public class DataFlightsService implements FlightsService {

  private final FlightsDao flightsDao;

  @Autowired
  public DataFlightsService(FlightsDao flightsDao) {
    this.flightsDao = flightsDao;
  }

  @Override
  public Flight getOrCreateFlight(FlightRoute route, LocalDate date) {
    Flight flight = flightsDao.getByRouteAndDate(route, date);
    if (flight == null) {
      MappedFlight entity = new MappedFlight();
      Plane plane = route.getPlane();
      if (plane != null) {
        entity.setAvailableSeats(plane.getTotalSeats());
      }
      entity.setDate(date);
      entity.setRoute((MappedFlightRoute) route);
      try {
        flight = flightsDao.saveAndFlush(entity);
      } catch (ConstraintViolationException e) {
        // Most probably someone created it.
        flight = flightsDao.getByRouteAndDate(route, date);
      }
    }
    return flight;
  }

  @Override
  public boolean registerSeat(User user, Flight flight, SeatClass clazz) {
    return false;
  }
}
