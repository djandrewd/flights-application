package ua.danit.flights.engine.impl.engines;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.engine.SearchResult;

/**
 * Simple travelling salesman problem solver using N over N selection.
 * Moved to utility class for reuse in few route engines implementation.
 *
 * @author Andrey Minov
 */
class CalculationEngine {
  private static final Logger LOGGER = LoggerFactory.getLogger(CachedServicesRouteEngine.class);
  private static final int PROC_NUMBER = Runtime.getRuntime().availableProcessors();

  /**
   * Calculate routes from provided airport with max number of provided stops.
   *
   * @param executorService      executors thread pool for routes calculation.
   * @param airports             list of airport provided for calculation.
   * @param maxPossibleStops     max possible stops during the route.
   * @param directRoutesSupplier supplier for providing direct routes between airports.
   * @param consumer             of calculated entries for given airports
   */
  static void calculateAirports(ExecutorService executorService, List<Airport> airports,
                                Function<Airport, List<FlightRoute>> directRoutesSupplier,
                                Consumer<SearchResult> consumer, int maxPossibleStops) {

    AtomicInteger size = new AtomicInteger(airports.size());
    AtomicInteger done = new AtomicInteger(0);
    int part = max(airports.size() / PROC_NUMBER, 1);
    Stream<int[]> slices = IntStream.range(0, PROC_NUMBER).mapToObj(
        i -> new int[] {min(airports.size(), i * part), min(airports.size(), (i + 1) * part)});
    CompletableFuture[] futures = slices.map(slice -> {
      CompletableFuture<Void> result = new CompletableFuture<>();
      executorService.submit(() -> {
        try {
          airports.subList(slice[0], slice[1]).stream().map(v -> new AbstractMap.SimpleEntry<>(v,
              calculateDeparture(directRoutesSupplier, v, maxPossibleStops))).forEach(paths -> {
                for (SearchResult path : paths.getValue()) {
                  consumer.accept(path);
                }
                LOGGER.info("Finished. Left airports {}, done with {}", size.decrementAndGet(),
                    done.incrementAndGet());
              });
          result.complete(null);
        } catch (Exception e) {
          result.completeExceptionally(e);
        }
      });
      return result;
    }).toArray(CompletableFuture[]::new);
    // Wait for all to complete
    CompletableFuture.allOf(futures).join();
  }

  private static List<SearchResult> calculateDeparture(
      Function<Airport, List<FlightRoute>> directRoutesSupplier, Airport departure,
      int maxPossibleStops) {
    // General rule of 'travelling salesman problem' is not to pass several times
    // same point.
    LOGGER.info("Calculate routes from {} with stops number {}", departure, maxPossibleStops);
    List<SearchResult> response = new ArrayList<>();
    Queue<SearchResult> pending = new LinkedList<>();
    pending.offer(SearchResult.of(departure));
    while (!pending.isEmpty()) {
      SearchResult path = pending.poll();
      if (path.pathSize() >= maxPossibleStops) {
        response.add(path);
        continue;
      }

      // Check do we visit any of children and copy routes at this point.
      for (FlightRoute route : directRoutesSupplier.apply(path.getCurrent())) {
        if (!path.isVisited(route)) {
          // Check is plane take off after initial has landed.
          if (path.getCurrentTime() != null && route.getDepartureTime() != null && path
              .getCurrentTime().isAfter(route.getDepartureTime())) {
            continue;
          }
          SearchResult move = path.copy().move(route);
          response.add(move);
          pending.offer(move);
        }
      }
    }
    return response;
  }
}
