package ua.danit.flights.data.impl.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.danit.flights.data.impl.model.MappedFlight;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;

/**
 * JPA data access object for {@link ua.danit.flights.data.model.Flight} objects.
 *
 * @author Andrey Minov
 */
public interface FlightsDao extends JpaRepository<MappedFlight, Long> {
  Flight getByRouteAndDate(FlightRoute flightRoute, LocalDate date);
}
