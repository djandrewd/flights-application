package ua.danit.flights.data.model;

/**
 * Aggregated entity for plane seats for classes.
 *
 * @author Andrey Minov
 */
public interface PlaneSeats {
  Plane getPlane();

  SeatClass getSeatClass();

  int getNumberOfSeats();

  int getStartRow();

  int getEndRow();

  int getSeatsInRow();
}
