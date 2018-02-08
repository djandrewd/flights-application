package ua.danit.flights.data.impl.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.data.impl.dao.AirlinesDao;
import ua.danit.flights.data.impl.dao.AirportsDao;
import ua.danit.flights.data.impl.dao.FlightRoutesDao;
import ua.danit.flights.data.impl.utils.Utils;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.openflights.services.OpenFlightsAirlinesService;
import ua.danit.flights.data.openflights.services.OpenFlightsAirportService;
import ua.danit.flights.data.openflights.services.OpenFlightsRoutesService;
import ua.danit.flights.data.services.AirportService;

/**
 * Test for data flights routes.
 *
 * @author Andrey Minov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataConfiguration.class})
public class DataRoutesServiceTest {

  private static OpenFlightsAirportService openFlightsAirportService;
  private static OpenFlightsRoutesService openFlightsRoutesService;

  @Autowired
  private DataRoutesService dataRoutesService;

  @Autowired
  private AirportService airportService;

  @Autowired
  private AirportsDao airportsDao;

  @Autowired
  private AirlinesDao airlinesDao;

  @Autowired
  private FlightRoutesDao flightRoutesDao;

  @BeforeClass
  public static void initExternal() {
    OpenFlightsAirlinesService openFlightsAirlinesService = new OpenFlightsAirlinesService();
    openFlightsAirportService = new OpenFlightsAirportService();
    openFlightsRoutesService =
        new OpenFlightsRoutesService(openFlightsAirlinesService, openFlightsAirportService);
  }

  @Before
  public void setUp() throws Exception {
    List<Airport> departures = openFlightsAirportService.getByIata("KBP");
    List<Airport> arrivals = openFlightsAirportService.getByIata("AMS");
    departures.stream().map(Utils.airportConvert()).forEach(airportsDao::save);
    arrivals.stream().map(Utils.airportConvert()).forEach(airportsDao::save);
    //
    List<FlightRoute> flightRoutes = new ArrayList<>();
    for (Airport departure : departures) {
      for (Airport arrival : arrivals) {
        flightRoutes.addAll(openFlightsRoutesService.getDirectRoutes(departure, arrival));
      }
    }
    flightRoutes.stream().map(FlightRoute::getOperator).distinct().map(Utils.airlineConvert())
                .forEach(airlinesDao::save);
    flightRoutes.stream().map(Utils.routeConvert()).forEach(flightRoutesDao::save);

  }

  @Test
  public void testFromKBP() throws Exception {
    Airport airport = airportService.getByIata("KBP").get(0);
    List<FlightRoute> routesFrom = dataRoutesService.getDirectRoutesFrom(airport);
    assertNotNull(routesFrom);
    assertFalse(routesFrom.isEmpty());
    routesFrom.forEach(v -> assertEquals(airport, v.getDeparture()));
  }

  @Test
  public void testKBPtoAMS() throws Exception {
    Airport dep = airportService.getByIata("KBP").get(0);
    Airport arr = airportService.getByIata("AMS").get(0);

    List<FlightRoute> routesBetween = dataRoutesService.getDirectRoutes(dep, arr);
    assertNotNull(routesBetween);

    routesBetween.forEach(v -> {
      assertEquals(dep, v.getDeparture());
      assertEquals(arr, v.getArrival());
    });
  }

}