package ua.danit.flights.data.impl.model;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ua.danit.flights.data.model.Airline;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.model.Plane;

/**
 * Model class holding information about available flight from point A to point B.
 * Holds information about departure and arrival airports, take off time, approximate
 * time of the landing, serving company,  schedule (daily, charter, or specific),
 * start and end date.
 *
 * @author Andrey Minov.
 */
@Entity
@Table(name = "FLIGHT_ROUTE")
public class MappedFlightRoute implements FlightRoute {
  @Id
  private long id;
  private String name;
  @Column(name = "START_DATE")
  private LocalDate startDate;
  @Column(name = "END_DATE")
  private LocalDate endDate;
  @Column(name = "DEPARTURE_TIME")
  private LocalTime departureTime;
  @Column(name = "ARRIVAL_TIME")
  private LocalTime arrivalTime;
  @Column(name = "DAILY_SCHEDULE")
  private Integer dailySchedule;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "DEPARTURE_AIRPORT_ID")
  private MappedAirport departure;
  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "ARRIVAL_AIRPORT_ID")
  private MappedAirport arrival;
  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "AIRLINE_ID")
  private MappedAirline operator;
  @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
  private MappedPlane plane;

  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public Airport getDeparture() {
    return departure;
  }

  public void setDeparture(MappedAirport departure) {
    this.departure = departure;
  }

  @Override
  public Airport getArrival() {
    return arrival;
  }

  public void setArrival(MappedAirport arrival) {
    this.arrival = arrival;
  }

  @Override
  public Airline getOperator() {
    return operator;
  }

  public void setOperator(MappedAirline operator) {
    this.operator = operator;
  }

  @Override
  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  @Override
  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  @Override
  public LocalTime getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(LocalTime departureTime) {
    this.departureTime = departureTime;
  }

  @Override
  public LocalTime getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  @Override
  public Integer getDailySchedule() {
    return dailySchedule;
  }

  public void setDailySchedule(int dailySchedule) {
    this.dailySchedule = dailySchedule;
  }

  @Override
  public Plane getPlane() {
    return plane;
  }

  public void setPlane(MappedPlane plane) {
    this.plane = plane;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
