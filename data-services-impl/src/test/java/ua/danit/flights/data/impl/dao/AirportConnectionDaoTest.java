package ua.danit.flights.data.impl.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.data.domain.Sort.Direction.ASC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.data.impl.model.AirportConnection;
import ua.danit.flights.data.impl.model.ConnectionRoute;
import ua.danit.flights.data.impl.utils.Utils;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.openflights.services.OpenFlightsAirlinesService;
import ua.danit.flights.data.openflights.services.OpenFlightsAirportService;
import ua.danit.flights.data.openflights.services.OpenFlightsRoutesService;
import ua.danit.flights.data.services.AirportService;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataConfiguration.class})
public class AirportConnectionDaoTest {

  private static OpenFlightsAirportService openFlightsAirportService;
  private static OpenFlightsRoutesService openFlightsRoutesService;

  @Autowired
  private AirportConnectionDao airportConnectionDao;

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
    Airport departure = openFlightsAirportService.getByIata("KBP").get(0);
    airportsDao.save(Utils.airportConvert().apply(departure));
    //
    Random random = new Random();
    AirportConnection airportConnection = new AirportConnection();
    List<ConnectionRoute> routes = new ArrayList<>();
    airportConnection.setRoutes(routes);
    airportConnection.setDeparture((Utils.airportConvert().apply(departure)));
    airportConnection.setArrival((Utils.airportConvert().apply(departure)));

    for (FlightRoute flightRoute : openFlightsRoutesService.getDirectRoutesFrom(departure)) {
      airlinesDao.save(Utils.airlineConvert().apply(flightRoute.getOperator()));
      airportsDao.save(Utils.airportConvert().apply(flightRoute.getArrival()));
      flightRoutesDao.save(Utils.routeConvert().apply(flightRoute));

      ConnectionRoute connectionRoute = new ConnectionRoute();
      connectionRoute.setFlightRoute(Utils.routeConvert().apply(flightRoute));
      connectionRoute.setPosition(random.nextInt(100));
      connectionRoute.setAirportConnection(airportConnection);
      routes.add(connectionRoute);
    }
    airportConnectionDao.save(airportConnection);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void selectAllRoutes() {
    Airport departure = airportService.findByIata("KBP").get(0);
    List<AirportConnection> connections = airportConnectionDao
        .getByDepartureAndArrivalAndStopsLessThanEqual(departure, departure, 10,
            new PageRequest(0, 10, new Sort(ASC, "stops")));
    assertNotNull(connections);
    assertFalse(connections.isEmpty());
    assertEquals(1, connections.size());

    AirportConnection connection = connections.get(0);
    assertNotNull(connection.getRoutes());
    assertFalse(connection.getRoutes().isEmpty());

    int pos = -1;
    for (ConnectionRoute route : connection.getRoutes()) {
      if (route.getPosition() < pos) {
        fail("Incorrect sorted position: " + route);
      }
    }
  }
}