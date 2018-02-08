package ua.danit.flights.data.model;

import java.time.LocalDate;

/**
 * Created by andreymi on 9/22/2017.
 */
public interface Flight {
  long getId();

  FlightRoute getRoute();

  LocalDate getDate();

  int getAvailableSeats();
}
