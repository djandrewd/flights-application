package ua.danit.flights.data.impl.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.danit.flights.data.impl.dao.FlightRoutesDao;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.RoutesService;

/**
 * Data service for {@link ua.danit.flights.data.model.FlightRoute} using data access object from
 * relational database.
 *
 * @author Andrey Minov
 */
@Service
public class DataRoutesService implements RoutesService {

  private final FlightRoutesDao flightRoutesDao;

  @Autowired
  public DataRoutesService(FlightRoutesDao flightRoutesDao) {
    this.flightRoutesDao = flightRoutesDao;
  }

  @Override
  public Optional<FlightRoute> getById(long id) {
    return Optional.ofNullable(flightRoutesDao.findOne(id));
  }

  @Override
  public List<FlightRoute> getDirectRoutes(Airport departure, Airport destination) {
    return flightRoutesDao.getByDepartureAndArrival(departure, destination);
  }

  @Override
  public List<FlightRoute> getDirectRoutesFrom(Airport departure) {
    return flightRoutesDao.getByDeparture(departure);
  }

  @Override
  public List<FlightRoute> getAll() {
    return flightRoutesDao.findAll().stream().map(v -> (FlightRoute) v)
                          .collect(Collectors.toList());
  }
}
