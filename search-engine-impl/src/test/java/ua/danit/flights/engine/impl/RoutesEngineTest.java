package ua.danit.flights.engine.impl;

import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;
import ua.danit.flights.engine.RoutesEngine;
import ua.danit.flights.engine.impl.engines.CachedServicesRouteEngine;

/**
 * Test several routes engine cases.
 *
 * @author Andrey Minov
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class RoutesEngineTest {

  @Mock
  private AirportService airportService;

  @Mock
  private RoutesService routesService;

  @Mock
  private Airport departure;

  @Mock
  private Airport arrival;


  private RoutesEngine routesEngine;

  @Before
  public void setUp() throws Exception {
    when(departure.getId()).thenReturn(-1L);
    when(arrival.getId()).thenReturn(-2L);
    //
    when(airportService.getByIata("A")).thenReturn(singletonList(departure));
    when(airportService.getByIata("B")).thenReturn(singletonList(arrival));
    when(airportService.getById(-1L)).thenReturn(of(departure));
    when(airportService.getById(-2L)).thenReturn(of(arrival));
    //
    routesEngine = new CachedServicesRouteEngine(routesService, airportService);
  }

  @Test
  public void testSamePoint() {
    assertTrue(routesEngine.findRoute(departure, departure, 1, 0, 10).isEmpty());
  }

  @Test
  public void testDirectRoute() {
    when(airportService.getAll()).thenReturn(Arrays.asList(departure, arrival));

    FlightRoute route = mock(FlightRoute.class);
    when(route.getId()).thenReturn(1L);
    when(route.getArrival()).thenReturn(arrival);
    when(route.getDeparture()).thenReturn(departure);
    when(routesService.getDirectRoutes(departure, arrival)).thenReturn(singletonList(route));
    when(routesService.getDirectRoutesFrom(departure)).thenReturn(singletonList(route));
    when(routesService.getById(1)).thenReturn(of(route));

    routesEngine.instantiate();
    assertEquals(singletonList(singletonList(route)),
        routesEngine.findRoute(departure, arrival, 1, 0, 10));
  }

  @Test
  public void testRouteWithOneStop() {
    // Intermidiate C and D
    Airport c = mock(Airport.class);
    when(c.getId()).thenReturn(1L);
    Airport d = mock(Airport.class);
    when(d.getId()).thenReturn(2L);
    when(airportService.getAll()).thenReturn(Arrays.asList(departure, c, d, arrival));


    FlightRoute routeAC = mock(FlightRoute.class);
    when(routeAC.getId()).thenReturn(1L);
    when(routeAC.getDeparture()).thenReturn(departure);
    when(routeAC.getArrival()).thenReturn(c);
    //s    when(routesService.getDirectRoutes(departure, c)).thenReturn(singletonList(routeAC));
    FlightRoute routeAD = mock(FlightRoute.class);
    when(routeAD.getId()).thenReturn(2L);
    when(routeAD.getDeparture()).thenReturn(departure);
    when(routeAD.getArrival()).thenReturn(d);
    //    when(routesService.getDirectRoutes(departure, d)).thenReturn(singletonList(routeAD));
    FlightRoute routeDB = mock(FlightRoute.class);
    when(routeDB.getId()).thenReturn(3L);
    when(routeDB.getDeparture()).thenReturn(d);
    when(routeDB.getArrival()).thenReturn(arrival);
    //    when(routesService.getDirectRoutes(d, arrival)).thenReturn(singletonList(routeDB));
    FlightRoute routeCB = mock(FlightRoute.class);
    when(routeCB.getId()).thenReturn(4L);
    when(routeCB.getDeparture()).thenReturn(c);
    when(routeCB.getArrival()).thenReturn(arrival);
    //    when(routesService.getDirectRoutes(c, arrival)).thenReturn(singletonList(routeCB));
    //
    when(routesService.getDirectRoutesFrom(departure)).thenReturn(Arrays.asList(routeAC, routeAD));
    when(routesService.getDirectRoutesFrom(c)).thenReturn(singletonList(routeCB));
    when(routesService.getDirectRoutesFrom(d)).thenReturn(singletonList(routeDB));
    when(routesService.getById(1)).thenReturn(of(routeAC));
    when(routesService.getById(2)).thenReturn(of(routeAD));
    when(routesService.getById(3)).thenReturn(of(routeDB));
    when(routesService.getById(4)).thenReturn(of(routeCB));

    routesEngine.instantiate();

    Collection<List<FlightRoute>> routes =
        new HashSet<>(routesEngine.findRoute(departure, arrival, 1, 0, 10));
    Collection<List<FlightRoute>> expected = new HashSet<>(
        Arrays.asList(Arrays.asList(routeAC, routeCB), Arrays.asList(routeAD, routeDB)));
    assertEquals(expected, routes);
  }

  @Test
  public void testDirectAndTwoStops() {
    Airport c = mock(Airport.class);
    when(c.getId()).thenReturn(1L);
    Airport d = mock(Airport.class);
    when(d.getId()).thenReturn(2L);

    //    when(airportService.getByIata("C")).thenReturn(Optional.of(c));
    //    when(airportService.getByIata("D")).thenReturn(Optional.of(d));
    when(airportService.getAll()).thenReturn(Arrays.asList(departure, arrival, c, d));

    // direct
    FlightRoute routeAB = mock(FlightRoute.class);
    when(routeAB.getId()).thenReturn(1L);
    when(routeAB.getDeparture()).thenReturn(departure);
    when(routeAB.getArrival()).thenReturn(arrival);
    //    when(routesService.getDirectRoutes(departure, arrival)).thenReturn(singletonList(routeAB));
    //
    FlightRoute routeAC = mock(FlightRoute.class);
    when(routeAC.getId()).thenReturn(2L);
    when(routeAC.getDeparture()).thenReturn(departure);
    when(routeAC.getArrival()).thenReturn(c);
    //    when(routesService.getDirectRoutes(departure, c)).thenReturn(singletonList(routeAC));
    //
    FlightRoute routeCD = mock(FlightRoute.class);
    when(routeCD.getId()).thenReturn(3L);
    when(routeCD.getDeparture()).thenReturn(c);
    when(routeCD.getArrival()).thenReturn(d);
    //    when(routesService.getDirectRoutes(c, d)).thenReturn(singletonList(routeCD));
    //
    FlightRoute routeDB = mock(FlightRoute.class);
    when(routeDB.getId()).thenReturn(4L);
    when(routeDB.getDeparture()).thenReturn(d);
    when(routeDB.getArrival()).thenReturn(arrival);
    //    when(routesService.getDirectRoutes(d, arrival)).thenReturn(singletonList(routeDB));
    //
    when(routesService.getDirectRoutesFrom(departure)).thenReturn(Arrays.asList(routeAB, routeAC));
    when(routesService.getDirectRoutesFrom(c)).thenReturn(singletonList(routeCD));
    when(routesService.getDirectRoutesFrom(d)).thenReturn(singletonList(routeDB));
    when(routesService.getById(1)).thenReturn(of(routeAB));
    when(routesService.getById(2)).thenReturn(of(routeAC));
    when(routesService.getById(3)).thenReturn(of(routeCD));
    when(routesService.getById(4)).thenReturn(of(routeDB));


    routesEngine.instantiate();

    assertEquals(singletonList(singletonList(routeAB)),
        routesEngine.findRoute(departure, arrival, 1, 0, 10));

    Collection<List<FlightRoute>> routes =
        new HashSet<>(routesEngine.findRoute(departure, arrival, 2, 0, 10));
    Collection<List<FlightRoute>> expected = new HashSet<>(
        Arrays.asList(Arrays.asList(routeAC, routeCD, routeDB), singletonList(routeAB)));

    assertEquals(expected, routes);


  }
}