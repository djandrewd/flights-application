package ua.danit.flights.data.openflights.model;

import com.google.common.base.MoreObjects;
import ua.danit.flights.data.model.Airport;

/**
 * Holds information about airport : id, short code,
 * country and city, longitude and latitude, timezone.
 *
 * @author Andrey Minov
 */
public class OpenFlightsAirport implements Airport {

  private long id;
  private String name;
  private String city;
  private String country;
  private String iataCode;
  private String icaoCode;
  private double longitude;
  private double latitude;
  private String timeZone;

  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String getIata() {
    return iataCode;
  }

  public void setIataCode(String iataCode) {
    this.iataCode = iataCode;
  }

  @Override
  public String getIcao() {
    return icaoCode;
  }

  public void setIcaoCode(String icaoCode) {
    this.icaoCode = icaoCode;
  }

  @Override
  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  @Override
  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  @Override
  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OpenFlightsAirport that = (OpenFlightsAirport) o;

    if (id != that.id) {
      return false;
    }
    if (Double.compare(that.longitude, longitude) != 0) {
      return false;
    }
    if (Double.compare(that.latitude, latitude) != 0) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (city != null ? !city.equals(that.city) : that.city != null) {
      return false;
    }
    if (country != null ? !country.equals(that.country) : that.country != null) {
      return false;
    }
    if (iataCode != null ? !iataCode.equals(that.iataCode) : that.iataCode != null) {
      return false;
    }
    if (icaoCode != null ? !icaoCode.equals(that.icaoCode) : that.icaoCode != null) {
      return false;
    }
    return timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null;
  }

  @Override
  public int hashCode() {
    int result;
    result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (iataCode != null ? iataCode.hashCode() : 0);
    result = 31 * result + (icaoCode != null ? icaoCode.hashCode() : 0);
    long temp = Double.doubleToLongBits(longitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(latitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("city", city)
                      .add("country", country).add("iataCode", iataCode).add("icaoCode", icaoCode)
                      .add("longitude", longitude).add("latitude", latitude)
                      .add("timeZone", timeZone).toString();
  }
}
