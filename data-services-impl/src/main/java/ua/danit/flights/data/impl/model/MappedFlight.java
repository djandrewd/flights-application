package ua.danit.flights.data.impl.model;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;

/**
 * Model class holding information about available flight from point A to point B.
 * Holds information about departure and arrival airports, take off time, approximate
 * time of the landing, serving company,  schedule (daily, charter, or specific),
 * start and end date.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "FLIGHT")
public class MappedFlight implements Flight {
  @Id
  private long id;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "FLIGHT_ROUTE_ID")
  private MappedFlightRoute route;

  @Column(name = "DATE")
  private LocalDate date;

  @Column(name = "SEATS_AVAILABLE")
  private int availableSeats;

  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public FlightRoute getRoute() {
    return route;
  }

  public void setRoute(MappedFlightRoute route) {
    this.route = route;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public int getAvailableSeats() {
    return availableSeats;
  }

  public void setAvailableSeats(int availableSeats) {
    this.availableSeats = availableSeats;
  }
}
