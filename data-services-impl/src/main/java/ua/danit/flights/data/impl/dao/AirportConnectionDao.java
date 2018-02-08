package ua.danit.flights.data.impl.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.danit.flights.data.impl.model.AirportConnection;
import ua.danit.flights.data.model.Airport;

/**
 * Data access object for {@link ua.danit.flights.data.impl.model.AirportConnection} objects
 */
public interface AirportConnectionDao extends JpaRepository<AirportConnection, Long> {
  List<AirportConnection> getByDepartureAndArrivalAndStopsLessThanEqual(Airport departure,
                                                                        Airport arrival,
                                                                        int stops,
                                                                        Pageable pageable);

  @Query("SELECT DISTINCT a.departure FROM AirportConnection a")
  List<Airport> getCalculatedAirports();
}
