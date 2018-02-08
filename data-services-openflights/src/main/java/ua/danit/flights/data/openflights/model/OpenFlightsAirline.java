package ua.danit.flights.data.openflights.model;

import com.google.common.base.MoreObjects;
import ua.danit.flights.data.model.Airline;

/**
 * Holds information about airline operators.
 *
 * @author Andrey Minov
 */
public class OpenFlightsAirline implements Airline {

  private long id;
  private String name;
  private String iata;
  private String icao;
  private String country;

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
  public String getIata() {
    return iata;
  }

  public void setIata(String iata) {
    this.iata = iata;
  }

  @Override
  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  @Override
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("iata", iata)
                      .add("icao", icao).add("country", country).toString();
  }
}
