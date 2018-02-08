package ua.danit.flights.api.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Holds information about airline: name, IAta, country.
 *
 * @author Andrey Minov.
 */
@JsonInclude(NON_NULL)
public class Airline {
  private String name;
  private String iata;
  private String country;

  /**
   * Instantiates a new Airline instance used for model.
   *
   * @param name    the himan readable name of the airline.
   * @param iata    the IATA code of the airline.
   * @param country the country where this airline has base airport.
   */
  public Airline(String name, String iata, String country) {
    this.name = name;
    this.iata = iata;
    this.country = country;
  }


  public String getName() {
    return name;
  }

  public String getIata() {
    return iata;
  }

  public String getCountry() {
    return country;
  }
}
