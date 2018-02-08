package ua.danit.flights.engine.impl.engines;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;
import static ua.danit.flights.data.impl.utils.Utils.convertToMapped;
import static ua.danit.flights.engine.impl.engines.CalculationEngine.calculateAirports;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.danit.flights.data.impl.model.AirportConnection;
import ua.danit.flights.data.impl.model.ConnectionRoute;
import ua.danit.flights.data.impl.services.DataAirportConnectionsService;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;
import ua.danit.flights.engine.ServicesRoutesEngine;

/**
 * Route engine which uses pre-calculated storage information about flights routes or
 * initialize data storage with values provided by data services.
 *
 * @author Andrey Minov
 */
public class DataServicesRouteEngine extends ServicesRoutesEngine {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataServicesRouteEngine.class);
  private static final int BATCH_SIZE = 400_000;
  private final int maxStops;
  private DataAirportConnectionsService airportConnectionsService;
  private ExecutorService executorService;
  private boolean initOnStart;

  /**
   * Instantiates a new Data services route engine.
   *
   * @param routesService             the routes service
   * @param airportService            the airport service
   * @param airportConnectionsService the airport connections service
   * @param executorService           the executor service
   * @param maxStops                  the max stops for calculation.
   * @param initOnStart               true if routes must be recalculated on start
   */
  public DataServicesRouteEngine(RoutesService routesService, AirportService airportService,
                                 DataAirportConnectionsService airportConnectionsService,
                                 ExecutorService executorService, int maxStops,
                                 boolean initOnStart) {
    super(routesService, airportService);
    this.airportConnectionsService = airportConnectionsService;
    this.executorService = executorService;
    this.maxStops = maxStops;
    this.initOnStart = initOnStart;
  }

  @Override
  public void instantiate() {
    if (!initOnStart) {
      return;
    }
    LOGGER.info("Start total airport recalculation!");
    LoadingCache<Airport, List<FlightRoute>> directRoutes =
        CacheBuilder.newBuilder().softValues().recordStats()
                    .build(new CacheLoader<Airport, List<FlightRoute>>() {
                      @Override
                      public List<FlightRoute> load(Airport key) throws Exception {
                        return getRoutesService().getDirectRoutesFrom(key);
                      }
                    });

    LoadingCache<Long, FlightRoute> routeById =
        CacheBuilder.newBuilder().softValues().recordStats()
                    .build(new CacheLoader<Long, FlightRoute>() {
                      @Override
                      public FlightRoute load(Long key) throws Exception {
                        return getRoutesService().getById(key).orElse(null);
                      }
                    });

    List<Airport> airports = getAirportService().getAll();
    airports.removeAll(airportConnectionsService.listOfCalculated());

    LinkedBlockingDeque<AirportConnection> queue = new LinkedBlockingDeque<>();
    calculateAirports(executorService, airports, v -> {
      try {
        return directRoutes.get(v);
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      }
    }, entry -> {
        try {
          AirportConnection connection = new AirportConnection();
          connection.setDeparture(convertToMapped(entry.getBeginning()));
          connection.setArrival(convertToMapped(entry.getCurrent()));
          List<ConnectionRoute> routes = new ArrayList<>(entry.getPath().size());
          for (int index = 0; index < entry.getPath().size(); index++) {
            ConnectionRoute route = new ConnectionRoute();
            route.setAirportConnection(connection);
            route.setPosition(index);
            route.setFlightRoute(convertToMapped(routeById.get(entry.getPath().get(index))));
            routes.add(route);
          }
          connection.setStops(entry.pathSize() - 1);
          connection.setRoutes(routes);
          queue.offer(connection);
          tryToStoreBatch(queue, BATCH_SIZE);
        } catch (Exception e) {
          LOGGER.error(e.getMessage(), e);
        }
      }, maxStops);

    tryToStoreBatch(queue, 0);
    LOGGER.info("All airports are calculated!");
  }

  @Override
  public Collection<List<FlightRoute>> findRoute(Airport departure, Airport arrival, int maxStops,
                                                 int pageNumber, int maxResults) {

    checkArgument(departure != null, "Departures airport value must not be empty!");
    checkArgument(arrival != null, "Destination airport value must not be empty!");
    checkArgument(pageNumber >= 0, "Page number cannot be negative!");
    checkArgument(maxResults > 0, "Max results numbers must be positive!");

    maxStops = Math.max(0, Math.min(maxStops, this.maxStops));
    if (departure.equals(arrival)) {
      return emptyList();
    }

    try {
      List<List<FlightRoute>> result = new ArrayList<>();
      List<AirportConnection> connections = airportConnectionsService
          .getConnectionRoutes(departure, arrival, maxStops, pageNumber, maxResults);
      for (AirportConnection connection : connections) {
        List<FlightRoute> routes =
            connection.getRoutes().stream().map(ConnectionRoute::getFlightRoute)
                      .map(r -> (FlightRoute) r).collect(Collectors.toList());
        result.add(routes);
      }
      return result;
    } catch (Exception e) {
      LOGGER.warn(String
          .format("Unable to calculate route [%s, %s]: %s", departure, arrival, e.getMessage()), e);
      throw new RuntimeException(e);
    }
  }

  private synchronized void tryToStoreBatch(BlockingQueue<AirportConnection> connections,
                                            int batchSize) {

    if (connections.size() > batchSize) {
      List<AirportConnection> airportConnections = new ArrayList<>();
      connections.drainTo(airportConnections, batchSize);
      try {
        LOGGER.info("Store {} calculated entries", airportConnections.size());
        airportConnectionsService.save(airportConnections);
        LOGGER.info("Store {} calculated entries done!", airportConnections.size());
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
        connections.addAll(airportConnections);
      }
    }
  }
}
