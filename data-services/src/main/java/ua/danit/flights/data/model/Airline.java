package ua.danit.flights.data.model;

/**
 * Holds information about airline operators.
 *
 * @author Andrey Minov
 */
public interface Airline {
  long getId();

  String getName();

  String getIata();

  String getIcao();

  String getCountry();
}
