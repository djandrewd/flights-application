package ua.danit.flights.data.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Model class holding information about available flight from point A to point B.
 * Holds information about departure and arrival airports, take off time, approximate
 * time of the landing, serving company,  schedule (daily, charter, or specific),
 * start and end date.
 *
 * @author Andrey Minov.
 */
public interface FlightRoute {
  long getId();

  Airport getDeparture();

  Airport getArrival();

  Airline getOperator();

  LocalDate getStartDate();

  LocalDate getEndDate();

  LocalTime getDepartureTime();

  LocalTime getArrivalTime();

  Integer getDailySchedule();

  Plane getPlane();

  String getName();
}
