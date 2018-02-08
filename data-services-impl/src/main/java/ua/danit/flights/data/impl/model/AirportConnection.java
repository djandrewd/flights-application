package ua.danit.flights.data.impl.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Holds information about two connected airports and route between them.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "AIRPORT_CONNECTION")
public class AirportConnection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "DEPARTURE_AIRPORT_ID")
  private MappedAirport departure;

  @ManyToOne
  @JoinColumn(name = "ARRIVAL_AIRPORT_ID")
  private MappedAirport arrival;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "airportConnection")
  @OrderBy("position")
  private List<ConnectionRoute> routes;

  private int stops;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public MappedAirport getDeparture() {
    return departure;
  }

  public void setDeparture(MappedAirport departure) {
    this.departure = departure;
  }

  public MappedAirport getArrival() {
    return arrival;
  }

  public void setArrival(MappedAirport arrival) {
    this.arrival = arrival;
  }

  public List<ConnectionRoute> getRoutes() {
    return routes;
  }

  public void setRoutes(List<ConnectionRoute> routes) {
    this.routes = routes;
  }

  public int getStops() {
    return stops;
  }

  public void setStops(int stops) {
    this.stops = stops;
  }
}
