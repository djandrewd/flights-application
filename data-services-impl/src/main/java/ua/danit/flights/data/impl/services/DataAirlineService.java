package ua.danit.flights.data.impl.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.danit.flights.data.impl.dao.AirlinesDao;
import ua.danit.flights.data.model.Airline;
import ua.danit.flights.data.services.AirlineService;

/**
 * Data service for {@link ua.danit.flights.data.model.Airline} using data access object from
 * relational database.
 *
 * @author Andrey Minov
 */
@Service
public class DataAirlineService implements AirlineService {

  private final AirlinesDao airlinesDao;

  @Autowired
  public DataAirlineService(AirlinesDao airlinesDao) {
    this.airlinesDao = airlinesDao;
  }

  @Override
  public Optional<Airline> getById(long id) {
    return Optional.ofNullable(airlinesDao.findOne(id));
  }

  @Override
  public List<Airline> getByIata(String iata) {
    return airlinesDao.getByIata(iata);
  }

  @Override
  public Collection<Airline> getAll() {
    return airlinesDao.findAll().stream().map(v -> (Airline) v).collect(Collectors.toList());
  }
}
