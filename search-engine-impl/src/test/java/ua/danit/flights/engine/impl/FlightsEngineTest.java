package ua.danit.flights.engine.impl;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.FlightsService;
import ua.danit.flights.engine.FlightsEngine;
import ua.danit.flights.engine.RoutesEngine;
import ua.danit.flights.engine.impl.engines.CachedServicesFlightsEngine;

/**
 * Test for correct implementation of flights engine.
 *
 * @author Andrey Minov
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class FlightsEngineTest {

  @Mock
  private FlightsService flightsService;
  @Mock
  private RoutesEngine routesEngine;

  private FlightsEngine flightsEngine;

  @Before
  public void setUp() throws Exception {
    flightsEngine = new CachedServicesFlightsEngine(flightsService, routesEngine);
  }

  @Test
  public void testCorrectDirectFlight() {
    FlightRoute direct = mock(FlightRoute.class);
    Airport departure = mock(Airport.class);
    Airport arrival = mock(Airport.class);

    when(direct.getDeparture()).thenReturn(departure);
    when(direct.getArrival()).thenReturn(arrival);
    when(direct.getDepartureTime()).thenReturn(LocalTime.of(12, 0));
    when(direct.getArrivalTime()).thenReturn(LocalTime.of(14, 0));

    LocalDate date = LocalDate.now();
    Flight flight = mock(Flight.class);
    when(flight.getAvailableSeats()).thenReturn(1);

    when(routesEngine.findRoute(departure, arrival, 1, 0, 10))
        .thenReturn(singletonList(singletonList(direct)));
    when(flightsService.getOrCreateFlight(direct, date)).thenReturn(flight);

    Collection<List<Flight>> flights =
        flightsEngine.searchFlights(departure, arrival, date, 1, 0, 10);
    assertEquals(singletonList(singletonList(flight)), flights);
  }


  @Test
  public void testNoSeatsLeft() {
    FlightRoute direct = mock(FlightRoute.class);
    Airport departure = mock(Airport.class);
    Airport arrival = mock(Airport.class);

    when(direct.getDeparture()).thenReturn(departure);
    when(direct.getArrival()).thenReturn(arrival);

    LocalDate date = LocalDate.now();
    Flight flight = mock(Flight.class);
    when(flight.getAvailableSeats()).thenReturn(0);

    when(routesEngine.findRoute(departure, arrival, 1, 0, 10))
        .thenReturn(singletonList(singletonList(direct)));
    when(flightsService.getOrCreateFlight(direct, date)).thenReturn(flight);

    Collection<List<Flight>> flights =
        flightsEngine.searchFlights(departure, arrival, date, 1, 0, 10);
    assertEquals(Collections.emptyList(), flights);
  }

  @Test
  public void testFlightExpired() {
    FlightRoute direct = mock(FlightRoute.class);
    Airport departure = mock(Airport.class);
    Airport arrival = mock(Airport.class);

    when(direct.getDeparture()).thenReturn(departure);
    when(direct.getArrival()).thenReturn(arrival);
    when(direct.getEndDate()).thenReturn(LocalDate.now().minusDays(1));

    LocalDate date = LocalDate.now();
    Flight flight = mock(Flight.class);
    when(flight.getAvailableSeats()).thenReturn(1);

    when(routesEngine.findRoute(departure, arrival, 1, 0, 10))
        .thenReturn(singletonList(singletonList(direct)));
    when(flightsService.getOrCreateFlight(direct, date)).thenReturn(flight);

    Collection<List<Flight>> flights =
        flightsEngine.searchFlights(departure, arrival, date, 1, 0, 10);
    assertEquals(Collections.emptyList(), flights);
  }

  @Test
  public void testFlightsConnectedOnNextDay() {
    Airport departure = mock(Airport.class);
    Airport stop = mock(Airport.class);
    Airport arrival = mock(Airport.class);

    // One take off at 22.00 and flight and 06.00 next day
    FlightRoute route1 = mock(FlightRoute.class);
    when(route1.getDeparture()).thenReturn(departure);
    when(route1.getArrival()).thenReturn(stop);
    when(route1.getDepartureTime()).thenReturn(LocalTime.of(22, 0));
    when(route1.getArrivalTime()).thenReturn(LocalTime.of(6, 0));
    // Next one will flight at 07.00 and till 11.00
    FlightRoute route2 = mock(FlightRoute.class);
    when(route2.getDeparture()).thenReturn(stop);
    when(route2.getArrival()).thenReturn(arrival);
    when(route2.getDepartureTime()).thenReturn(LocalTime.of(7, 0));
    when(route2.getArrivalTime()).thenReturn(LocalTime.of(11, 0));

    LocalDate date = LocalDate.now();
    Flight flight = mock(Flight.class);
    when(flight.getAvailableSeats()).thenReturn(1);

    when(routesEngine.findRoute(departure, arrival, 1, 0, 10))
        .thenReturn(singletonList(Arrays.asList(route1, route2)));

    when(flightsService.getOrCreateFlight(any(FlightRoute.class), any(LocalDate.class)))
        .thenReturn(flight);

    Collection<List<Flight>> flights =
        flightsEngine.searchFlights(departure, arrival, date, 1, 0, 10);
    assertEquals(singletonList(Arrays.asList(flight, flight)), flights);

    verify(flightsService, times(1)).getOrCreateFlight(route1, date);
    verify(flightsService, times(1)).getOrCreateFlight(route2, date.plusDays(1));

  }

}