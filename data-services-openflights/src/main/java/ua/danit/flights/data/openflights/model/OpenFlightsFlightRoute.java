package ua.danit.flights.data.openflights.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class OpenFlightsFlightRoute implements FlightRoute {
  private long id;
  private String name;
  private Airport departure;
  private Airport arrival;
  private Airline operator;
  private Plane plane;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime departureTime;
  private LocalTime arrivalTime;
  private Integer dailySchedule;

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

  public void setDeparture(Airport departure) {
    this.departure = departure;
  }

  @Override
  public Airport getArrival() {
    return arrival;
  }

  public void setArrival(Airport arrival) {
    this.arrival = arrival;
  }

  @Override
  public Airline getOperator() {
    return operator;
  }

  public void setOperator(Airline operator) {
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

  public void setPlane(Plane plane) {
    this.plane = plane;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "OpenFlightsFlightRoute{" +
            "name='" + name + '\'' +
            ", departure=" + departure +
            ", arrival=" + arrival +
            ", operator=" + operator +
            '}';
  }
}
