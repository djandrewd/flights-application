package ua.danit.flights.data.impl.services;

import static org.springframework.data.domain.Sort.Direction.ASC;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.danit.flights.data.impl.dao.AirportConnectionDao;
import ua.danit.flights.data.impl.dao.AirportConnectionStoreDao;
import ua.danit.flights.data.impl.model.AirportConnection;
import ua.danit.flights.data.model.Airport;

/**
 * Service for managing {@link ua.danit.flights.data.impl.model.AirportConnection} objects
 * and related functionality.
 *
 * @author Andrey Minov
 */
@Service
public class DataAirportConnectionsService {

  private final AirportConnectionDao airportConnectionDao;
  private final AirportConnectionStoreDao airportConnectionStoreDao;

  /**
   * Instantiates a new Data airport connections service.
   *
   * @param airportConnectionDao      the airport connection data service.
   * @param airportConnectionStoreDao the airport connection store data access object.
   */
  @Autowired
  public DataAirportConnectionsService(AirportConnectionDao airportConnectionDao,
                                       AirportConnectionStoreDao airportConnectionStoreDao) {
    this.airportConnectionDao = airportConnectionDao;
    this.airportConnectionStoreDao = airportConnectionStoreDao;
  }

  /**
   * Gets connection routes stored in data store between two airports with define number
   * of stops.
   *
   * @param departure  the departure airport of the route.
   * @param arrival    the arrival airport of the route
   * @param maxStops   the max stops of the requested route.
   * @param pageNumber the page number of the request started from 0.
   * @param maxResults the max results on the selection page.
   * @return the connection routes connecting two provided airports with define number of stops.
   */
  public List<AirportConnection> getConnectionRoutes(Airport departure, Airport arrival,
                                                     int maxStops, int pageNumber, int maxResults) {
    return airportConnectionDao
        .getByDepartureAndArrivalAndStopsLessThanEqual(departure, arrival, maxStops,
            new PageRequest(pageNumber, maxResults, new Sort(ASC, "stops")));
  }

  /**
   * Transactional saves list of entries into database using list JDBC.
   * This method should be used for storing huge amount of connection between airports.
   *
   * @param connections the collection of airport entries to be saved.
   */
  public void save(List<AirportConnection> connections) {
    airportConnectionStoreDao.save(connections);
  }

  /**
   * Return list of airports for which airport connections are already calculated.
   *
   * @return the list of airports with calculated airport connections.
   */
  public List<Airport> listOfCalculated() {
    return airportConnectionDao.getCalculatedAirports();
  }
}
