package ua.danit.flights.data.impl.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ua.danit.flights.data.model.Airline;

/**
 * Holds information about airline operators.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "AIRLINE")
public class MappedAirline implements Airline {

  @Id
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
}
