package ua.danit.flights.data.impl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.danit.flights.data.impl.model.MappedAirline;
import ua.danit.flights.data.model.Airline;

/**
 * JPA data access object for {@link ua.danit.flights.data.model.Airline} objects.
 *
 * @author Andrey Minov
 */
public interface AirlinesDao extends JpaRepository<MappedAirline, Long> {
  List<Airline> getByIata(String value);
}
