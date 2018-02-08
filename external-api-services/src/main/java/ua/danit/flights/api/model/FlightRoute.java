package ua.danit.flights.api.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Holds information about flight route: holding the flights inside and estimate price.
 *
 * @author Andrey Minov
 */
@JsonInclude(NON_NULL)
public class FlightRoute {

  private Airport departure;
  private Airport arrival;
  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  private ZonedDateTime departureTime;
  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  private ZonedDateTime arrivalTime;
  private BigDecimal price;
  private List<Flight> flights;

  /**
   * Instantiates a new Flight route between two provided airports.
   *
   * @param departure     departure airport and start of the route.
   * @param arrival       arrival airport and end of the route.
   * @param departureTime departure time at airport TZ at start of the route.
   * @param arrivalTime   arrival time at airport TZ at end of the route.
   * @param flights       the list of direct flights used which created single route.
   * @param price         the price of the flight route.
   */
  public FlightRoute(Airport departure, Airport arrival, ZonedDateTime departureTime,
                     ZonedDateTime arrivalTime, BigDecimal price, List<Flight> flights) {
    this(flights, price);
    this.departure = departure;
    this.arrival = arrival;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.price = price;
    this.flights = flights;
  }

  /**
   * Instantiates a new Flight route between two provided airports.
   *
   * @param flights the list of direct flights used which created single route.
   * @param price   the price of the flight route.
   */
  public FlightRoute(List<Flight> flights, BigDecimal price) {
    this.flights = flights;
    this.price = price;
  }

  public List<Flight> getFlights() {
    return flights;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Airport getDeparture() {
    return departure;
  }

  public Airport getArrival() {
    return arrival;
  }

  public ZonedDateTime getDepartureTime() {
    return departureTime;
  }

  public ZonedDateTime getArrivalTime() {
    return arrivalTime;
  }

  /**
   * Flight type presented direct flight on defined date.
   */
  @JsonInclude(NON_NULL)
  public static class Flight {
    private String name;
    private Airport departure;
    private Airport arrival;
    private Airline airline;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime departureTime;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime arrivalTime;

    /**
     * Instantiates a new Flight information.
     *
     * @param name          name of the flight.
     * @param departure     the departure of the flight part.
     * @param arrival       the arrival airport of the flight.
     * @param airline       the airline which operates the flight.
     * @param departureTime the departure time of the flight.
     * @param arrivalTime   the arrival time of the flight.
     */
    public Flight(String name, Airport departure, Airport arrival, Airline airline,
                  ZonedDateTime departureTime, ZonedDateTime arrivalTime) {
      this.name = name;
      this.departure = departure;
      this.arrival = arrival;
      this.airline = airline;
      this.departureTime = departureTime;
      this.arrivalTime = arrivalTime;
    }

    public Airport getDeparture() {
      return departure;
    }

    public Airport getArrival() {
      return arrival;
    }

    public Airline getAirline() {
      return airline;
    }

    public ZonedDateTime getDepartureTime() {
      return departureTime;
    }

    public ZonedDateTime getArrivalTime() {
      return arrivalTime;
    }

    public String getName() {
      return name;
    }
  }
}
