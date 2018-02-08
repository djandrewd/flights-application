package ua.danit.flights.data.model;

import java.util.List;

/**
 * Describe information about plane: number, classes, number of the seats, manifacturer.
 *
 * @author Andrey Minov
 */
public interface Plane {
  long getId();

  String getType();

  String getManufacturer();

  int getTotalSeats();

  List<PlaneSeats> getPlaneSeats();
}
