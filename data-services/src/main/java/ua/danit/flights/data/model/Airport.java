package ua.danit.flights.data.model;

/**
 * Holds information about airport : id, short code,
 * country and city, longitude and latitude, timezone.
 *
 * @author Andrey Minov
 */
public interface Airport {
  long getId();

  String getName();

  String getCity();

  String getCountry();

  String getIata();

  String getIcao();

  double getLongitude();

  double getLatitude();

  String getTimeZone();
}
