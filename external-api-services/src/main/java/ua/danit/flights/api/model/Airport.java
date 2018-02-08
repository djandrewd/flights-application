package ua.danit.flights.api.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;

/**
 * Holds information about airport: id, IATA, city, country, locations and timezone.
 *
 * @author Andrey Minov
 */
@JsonInclude(NON_NULL)
public class Airport {
  private String iata;
  private String name;
  private String city;
  private String country;
  private String timeZone;

  /**
   * Instantiates a new Airport.
   *
   * @param iata     the IATA code of the airport.
   * @param name     the human readable name of the airport.
   * @param city     the city where this airport is presented.
   * @param country  the country where this airport is operated.
   * @param timeZone the time zone of the airport
   */
  public Airport(String iata, String name, String city, String country, String timeZone) {
    this.iata = iata;
    this.name = name;
    this.city = city;
    this.country = country;
    this.timeZone = timeZone;
  }

  public String getIata() {
    return iata;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getTimeZone() {
    return timeZone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Airport airport = (Airport) o;

    if (iata != null ? !iata.equals(airport.iata) : airport.iata != null) {
      return false;
    }
    if (name != null ? !name.equals(airport.name) : airport.name != null) {
      return false;
    }
    if (city != null ? !city.equals(airport.city) : airport.city != null) {
      return false;
    }
    if (country != null ? !country.equals(airport.country) : airport.country != null) {
      return false;
    }
    return timeZone != null ? timeZone.equals(airport.timeZone) : airport.timeZone == null;
  }

  @Override
  public int hashCode() {
    int result = iata != null ? iata.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("iata", iata).add("name", name).add("city", city)
                      .add("country", country).add("timeZone", timeZone).toString();
  }
}
