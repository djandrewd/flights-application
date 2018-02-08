package ua.danit.flights.engine.impl.engines;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;
import static ua.danit.flights.engine.impl.engines.CalculationEngine.calculateAirports;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;
import ua.danit.flights.engine.ServicesRoutesEngine;

/**
 * Cached implementation of route engine. Solving 'travelling salesman problem'
 * by single caching of all results. Initialize algorithm still works by N over N selection.
 *
 * @author Andrey Minov
 */
public class CachedServicesRouteEngine extends ServicesRoutesEngine {
  private static final Logger LOGGER = LoggerFactory.getLogger(CachedServicesRouteEngine.class);

  private static final int MAX_POSSIBLE_STOPS = 3;
  private static final long EXPIRATION_HOURS = 2;
  private static final int PROC_NUMBER = Runtime.getRuntime().availableProcessors();

  private final int maxPossibleStops;

  private final LoadingCache<SearchKey, List<List<Long>>> routes;
  private final LoadingCache<Airport, List<FlightRoute>> directRoutes;
  private final LoadingCache<Long, FlightRoute> routesById;
  private final boolean calculateOnStart;
  private final ExecutorService executorService;

  /**
   * Instantiates a new Cached services route engine.
   *
   * @param routesService  the routes service
   * @param airportService the airport service
   */
  public CachedServicesRouteEngine(RoutesService routesService, AirportService airportService) {
    this(routesService, airportService, MAX_POSSIBLE_STOPS,
        Executors.newFixedThreadPool(PROC_NUMBER), false);
  }

  /**
   * Instantiates a new Cached services route engine.
   *
   * @param routesService    the routes service
   * @param airportService   the airport service
   * @param maxPossibleStops the max possible stops
   * @param executorService  the executor service for routes calculation.
   * @param calculateOnStart true if all routes must be calculated on the start.
   */
  public CachedServicesRouteEngine(RoutesService routesService, AirportService airportService,
                                   int maxPossibleStops, ExecutorService executorService,
                                   boolean calculateOnStart) {
    super(routesService, airportService);
    this.calculateOnStart = calculateOnStart;
    this.maxPossibleStops = maxPossibleStops;
    this.executorService = executorService;
    this.routesById = CacheBuilder.newBuilder().concurrencyLevel(PROC_NUMBER)
                                  .expireAfterAccess(EXPIRATION_HOURS, TimeUnit.HOURS).softValues()
                                  .recordStats().build(new CacheLoader<Long, FlightRoute>() {
                                    @Override
                                    public FlightRoute load(Long key) throws Exception {
                                      return routesService.getById(key).orElse(null);
                                    }
                                  });
    this.directRoutes = CacheBuilder.newBuilder().concurrencyLevel(PROC_NUMBER)
                                    .expireAfterAccess(EXPIRATION_HOURS, TimeUnit.HOURS)
                                    .softValues().recordStats()
                                    .build(new CacheLoader<Airport, List<FlightRoute>>() {
                                      @Override
                                      public List<FlightRoute> load(Airport key) throws Exception {
                                        return routesService.getDirectRoutesFrom(key);
                                      }
                                    });
    this.routes = CacheBuilder.newBuilder().expireAfterAccess(EXPIRATION_HOURS, TimeUnit.HOURS)
                              .concurrencyLevel(PROC_NUMBER).recordStats().softValues()
                              .build(new MissingCacheLoader());
  }

  @Override
  public Collection<List<FlightRoute>> findRoute(Airport departure, Airport arrival, int maxStops,
                                                 int pageNumber, int maxResults) {

    checkArgument(departure != null, "Departures airport value must not be empty!");
    checkArgument(arrival != null, "Destination airport value must not be empty!");
    checkArgument(pageNumber >= 0, "Page number cannot be negative!");
    checkArgument(maxResults > 0, "Max results numbers must be positive!");

    maxStops = Math.max(0, Math.min(maxStops, maxPossibleStops));

    try {
      if (departure.equals(arrival)) {
        return emptyList();
      }

      List<List<FlightRoute>> result = new ArrayList<>();
      SearchKey key = new SearchKey(departure.getId(), arrival.getId());
      List<List<Long>> values = routes.get(key);

      int startPage = Math.min(pageNumber * maxResults, values.size());
      int endPage = Math.min((pageNumber + 1) * maxResults, values.size());

      for (List<Long> val : values.subList(startPage, endPage)) {
        if (val.size() - 1 > maxStops) {
          break;
        }
        List<FlightRoute> route = new ArrayList<>(val.size());
        for (long routeId : val) {
          route.add(routesById.get(routeId));
        }
        result.add(route);
      }
      return result;
    } catch (ExecutionException e) {
      LOGGER.warn(String
          .format("Unable to calculate route [%s, %s]: %s", departure, arrival, e.getMessage()), e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void instantiate() {
    if (calculateOnStart) {
      try {
        List<FlightRoute> flightRoutes = getRoutesService().getAll();
        for (FlightRoute route : flightRoutes) {
          List<FlightRoute> data = directRoutes.get(route.getDeparture(), ArrayList::new);
          data.add(route);
        }

        calculateAirports(executorService, getAirportService().getAll(), v -> {
          try {
            return directRoutes.get(v);
          } catch (ExecutionException e) {
            throw new RuntimeException(e);
          }
        }, e -> {
            SearchKey key = new SearchKey(e.getBeginning().getId(), e.getCurrent().getId());
            try {
              List<List<Long>> present = routes.get(key, ArrayList::new);
              present.add(e.getPath());
            } catch (Exception ex) {
              throw new RuntimeException(ex);
            }
          }, maxPossibleStops);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static class SearchKey {
    private long departure;
    private long arrival;

    /**
     * Instantiates a new Search key.
     *
     * @param departure the departure
     * @param arrival   the arrival
     */
    SearchKey(long departure, long arrival) {
      this.departure = departure;
      this.arrival = arrival;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      SearchKey key = (SearchKey) o;
      return departure == key.departure && arrival == key.arrival;
    }

    @Override
    public int hashCode() {
      int result = (int) (departure ^ (departure >>> 32));
      result = 31 * result + (int) (arrival ^ (arrival >>> 32));
      return result;
    }
  }

  /////////////////////////////////////////////////
  // Help classes.
  /////////////////////////////////////////////////
  private class MissingCacheLoader extends CacheLoader<SearchKey, List<List<Long>>> {
    private static final int LOCK_HASH_MAX_NUM = 1 << 10;
    private Lock[] locks = new Lock[LOCK_HASH_MAX_NUM];

    /**
     * Instantiates a new Missing cache loader.
     */
    MissingCacheLoader() {
      IntStream.range(0, LOCK_HASH_MAX_NUM).forEach(i -> locks[i] = new ReentrantLock());
    }

    @Override
    public List<List<Long>> load(SearchKey key) throws Exception {
      Airport airport =
          getAirportService().getById(key.departure).orElseThrow(IllegalArgumentException::new);
      List<List<Long>> result = new ArrayList<>();
      Lock lock = locks[(int) airport.getId() & (LOCK_HASH_MAX_NUM - 1)];
      // Update add entries. Some may be calculated twice.
      // To avoid this synch of departure will be used.
      lock.lock();
      try {
        calculateAirports(executorService, Collections.singletonList(airport), v -> {
          try {
            return directRoutes.get(v);
          } catch (ExecutionException e) {
            throw new RuntimeException(e);
          }
        }, e -> {
            // By now only speed and space (which make collection of GC pauses up to 60 seconds!).
            if (e.getCurrent().getId() == key.arrival) {
              result.add(e.getPath());
            }
          }, maxPossibleStops);
      } finally {
        lock.unlock();
      }

      return result;
    }
  }

}
