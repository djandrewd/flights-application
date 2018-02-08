package ua.danit.flights.engine;

import static com.google.common.base.Preconditions.checkArgument;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;


/**
 * Calculation search result for tree of routes traversing.
 *
 * @author Andrey Minov
 */
public class SearchResult {
  private Set<Long> visited;
  private List<Long> path;

  private Airport beginning;
  private Airport current;
  private LocalTime currentTime;

  /**
   * Create new search result which starts from current airpoirt.
   *
   * @param current the current starting airport
   * @return the search result as initial point of all routes comes from current airport.
   */
  public static SearchResult of(Airport current) {
    SearchResult r = new SearchResult();
    r.path = new ArrayList<>();
    r.visited = new HashSet<>();
    r.visited.add(current.getId());
    r.current = current;
    r.beginning = current;
    r.currentTime = null;
    return r;
  }

  /**
   * Path size of current search result
   *
   * @return the size of the path.
   */
  public int pathSize() {
    return path.size();
  }

  /**
   * Checks if arrival node of the route is already visited.
   *
   * @param route the route to move
   * @return true in case node is already visited, false otherwise.
   */
  public boolean isVisited(FlightRoute route) {
    return visited.contains(route.getArrival().getId());
  }

  /**
   * Gets current node where route is standing.
   *
   * @return the current node where
   */
  public Airport getCurrent() {
    return current;
  }

  /**
   * Gets beginning of the current route.
   *
   * @return the beginning
   */
  public Airport getBeginning() {
    return beginning;
  }

  /**
   * Get current moving path.
   *
   * @return current moves of the path.
   */
  public List<Long> getPath() {
    return path;
  }

  /**
   * Get time at current airport when plane is landed.
   *
   * @return time at current airport when plane is landed.
   */
  public LocalTime getCurrentTime() {
    return currentTime;
  }

  /**
   * Copy search result into new one.
   * On every point that we do not visited we simply copy the route.
   *
   * @return the newly created instance of search result.
   */
  public SearchResult copy() {
    SearchResult copy = new SearchResult();
    copy.visited = new HashSet<>(visited);
    copy.path = new ArrayList<>(path);
    copy.beginning = beginning;
    copy.current = current;
    copy.currentTime = currentTime;
    return copy;
  }

  /**
   * Move route into new route
   *
   * @param route the route to move
   * @return current route modified by current move.
   */
  public SearchResult move(FlightRoute route) {
    checkArgument(route.getArrival() != null, "Arrival point must not be null!");
    checkArgument(route.getDeparture() != null, "Departure point must not be null!");
    checkArgument(route.getId() > 0, "Route id must be positive value!");

    path.add(route.getId());
    Airport point = route.getArrival();
    visited.add(point.getId());
    current = point;
    currentTime = route.getArrivalTime();
    return this;
  }
}
