package ua.danit.flights.data.openflights.services;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;

/**
 * Test for OpenFlights data.
 */
public class OpenFlightsRoutesServiceTest {

  private RoutesService routesService;
  private AirportService airportService;

  @Before
  public void setUp() throws Exception {
    airportService = new OpenFlightsAirportService();
    routesService = new OpenFlightsRoutesService(new OpenFlightsAirlinesService(), airportService);
  }

  @Test
  public void testKbpToAms() {
    Airport departure = airportService.getByIata("KBP").get(0);
    Airport arrival = airportService.getByIata("AMS").get(0);
    List<FlightRoute> routes = routesService.getDirectRoutes(departure, arrival);
    assertFalse(routes.isEmpty());
  }
}