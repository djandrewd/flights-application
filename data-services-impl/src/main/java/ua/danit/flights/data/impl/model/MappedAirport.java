package ua.danit.flights.data.impl.model;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ua.danit.flights.data.model.Airport;

/**
 * Holds information about airport : id, short code,
 * country and city, longitude and latitude, timezone.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "AIRPORT")
public class MappedAirport implements Airport {

  @Id
  private long id;
  private String name;
  private String city;
  private String country;
  private String iata;
  private String icao;
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

  public String getIata() {
    return iata;
  }

  public void setIata(String iata) {
    this.iata = iata;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
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

    MappedAirport airport = (MappedAirport) o;

    if (id != airport.id) {
      return false;
    }
    if (Double.compare(airport.longitude, longitude) != 0) {
      return false;
    }
    if (Double.compare(airport.latitude, latitude) != 0) {
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
    if (iata != null ? !iata.equals(airport.iata) : airport.iata != null) {
      return false;
    }
    if (icao != null ? !icao.equals(airport.icao) : airport.icao != null) {
      return false;
    }
    return timeZone != null ? timeZone.equals(airport.timeZone) : airport.timeZone == null;
  }

  @Override
  public int hashCode() {
    int result;
    result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (iata != null ? iata.hashCode() : 0);
    result = 31 * result + (icao != null ? icao.hashCode() : 0);
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
                      .add("country", country).add("iata", iata).add("icao", icao)
                      .add("longitude", longitude).add("latitude", latitude)
                      .add("timeZone", timeZone).toString();
  }
}
