package ua.danit.flights.data.impl.utils;

import java.util.function.Function;

import ua.danit.flights.data.impl.model.MappedAirline;
import ua.danit.flights.data.impl.model.MappedAirport;
import ua.danit.flights.data.impl.model.MappedFlightRoute;
import ua.danit.flights.data.model.Airline;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;

/**
 * Convert and test utils in order not to duplicate code.
 *
 * @author Andrey Minov
 */
public class Utils {

  private static final Function<Airport, MappedAirport> AIRPORT_MAPPED_FUNCTION = v -> {
    MappedAirport mappedAirport = new MappedAirport();
    mappedAirport.setId(v.getId());
    mappedAirport.setCity(v.getCity());
    mappedAirport.setCountry(v.getCountry());
    mappedAirport.setIata(v.getIata());
    mappedAirport.setIcao(v.getIcao());
    mappedAirport.setLatitude(v.getLatitude());
    mappedAirport.setLongitude(v.getLongitude());
    mappedAirport.setName(v.getName());
    mappedAirport.setTimeZone(v.getTimeZone());
    return mappedAirport;
  };

  private static final Function<Airline, MappedAirline> AIRLINE_MAPPED_FUNCTION = v -> {
    MappedAirline mappedAirline = new MappedAirline();
    mappedAirline.setId(v.getId());
    mappedAirline.setIata(v.getIata());
    mappedAirline.setIcao(v.getIata());
    mappedAirline.setCountry(v.getCountry());
    mappedAirline.setName(v.getName());
    return mappedAirline;
  };

  /**
   * Convert interface airport for
   * database mapped airport.
   *
   * @return the entity of database mapped airport
   */
  public static MappedAirport convertToMapped(Airport airport) {
    return airportConvert().apply(airport);
  }

  /**
   * Convert interface flight route for
   * database mapped flight route.
   *
   * @return the entity of database mapped flight route
   */
  public static MappedFlightRoute convertToMapped(FlightRoute flightRoute) {
    return routeConvert().apply(flightRoute);
  }

  /**
   * Airport convert function between general airport and mapped to database
   * entity.
   *
   * @return the convert function between general airport and mapped to database
   *         entity
   */
  public static Function<Airport, MappedAirport> airportConvert() {
    return AIRPORT_MAPPED_FUNCTION;
  }

  /**
   * Airline convert function between general airline and mapped to database
   * entity.
   *
   * @return the convert function between general airline and mapped to database
   *        entity
   */
  public static Function<Airline, MappedAirline> airlineConvert() {
    return AIRLINE_MAPPED_FUNCTION;
  }

  /**
   * Route convert function to convert interface flight route for
   * database mapped flight route.
   *
   * @return the function to convert interface flight route for
   *        database mapped flight route
   */
  public static Function<FlightRoute, MappedFlightRoute> routeConvert() {
    return v -> {
      MappedFlightRoute route = new MappedFlightRoute();
      route.setId(v.getId());
      route.setDeparture(AIRPORT_MAPPED_FUNCTION.apply(v.getDeparture()));
      route.setArrival(AIRPORT_MAPPED_FUNCTION.apply(v.getArrival()));
      route.setOperator(AIRLINE_MAPPED_FUNCTION.apply(v.getOperator()));
      return route;
    };
  }


}
