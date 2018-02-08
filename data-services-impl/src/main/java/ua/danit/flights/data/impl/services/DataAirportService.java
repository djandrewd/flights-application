package ua.danit.flights.data.impl.services;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;
import static org.springframework.data.domain.ExampleMatcher.matching;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ua.danit.flights.data.impl.dao.AirportsDao;
import ua.danit.flights.data.impl.model.MappedAirport;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.services.AirportService;

/**
 * Data service for {@link ua.danit.flights.data.model.Airport} using data access object from
 * relational database.
 *
 * @author Andrey Minov
 */
@Service
public class DataAirportService implements AirportService {
  private final AirportsDao airportsDao;

  @Autowired
  public DataAirportService(AirportsDao airportsDao) {
    this.airportsDao = airportsDao;
  }

  @Override
  public Optional<Airport> getById(long id) {
    return Optional.ofNullable(airportsDao.findOne(id));
  }

  @Override
  public List<Airport> getByIata(String iata) {
    MappedAirport airport = new MappedAirport();
    airport.setIata(iata);
    Example<MappedAirport> example = Example.of(airport,
        matching().withIgnorePaths("id", "longitude", "latitude").withIgnoreNullValues()
                  .withIgnoreCase());
    return airportsDao.findAll(example).stream().map(v -> (Airport) v).collect(Collectors.toList());
  }

  @Override
  public List<Airport> getAll() {
    return airportsDao.findAll().stream().map(v -> (Airport) v).collect(Collectors.toList());
  }

  @Override
  public List<Airport> findByCity(String city) {
    MappedAirport airport = new MappedAirport();
    airport.setCity(city);
    Example<MappedAirport> example = Example.of(airport,
        matching().withIgnorePaths("id", "longitude", "latitude").withIgnoreNullValues()
                  .withIgnoreCase().withStringMatcher(CONTAINING));
    return airportsDao.findAll(example).stream().map(v -> (Airport) v).collect(Collectors.toList());
  }

  @Override
  public List<Airport> findByIata(String iata) {
    MappedAirport airport = new MappedAirport();
    airport.setIata(iata);
    Example<MappedAirport> example = Example.of(airport,
        matching().withIgnorePaths("id", "longitude", "latitude").withIgnoreNullValues()
                  .withIgnoreCase().withStringMatcher(CONTAINING));
    return airportsDao.findAll(example).stream().map(v -> (Airport) v).collect(Collectors.toList());
  }
}
