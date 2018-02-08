package ua.danit.flights.data.impl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.danit.flights.data.impl.model.MappedAirport;

/**
 * JPA data access object for {@link ua.danit.flights.data.model.Airport} objects.
 *
 * @author Andrey Minov
 */
public interface AirportsDao extends JpaRepository<MappedAirport, Long> {
}
