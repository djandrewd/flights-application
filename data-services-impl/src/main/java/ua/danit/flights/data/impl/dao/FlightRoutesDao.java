package ua.danit.flights.data.impl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.danit.flights.data.impl.model.MappedFlightRoute;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;

/**
 * JPA data access object for {@link ua.danit.flights.data.model.FlightRoute} objects.
 *
 * @author Andrey Minov
 */
public interface FlightRoutesDao extends JpaRepository<MappedFlightRoute, Long> {
  List<FlightRoute> getByDepartureAndArrival(Airport departure, Airport arrival);

  List<FlightRoute> getByDeparture(Airport departure);
}
