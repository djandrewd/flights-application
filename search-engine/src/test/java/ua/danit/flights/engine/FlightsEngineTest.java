package ua.danit.flights.engine;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.services.FlightsService;

/**
 * Test for correct implementatation of flights engine.
 *
 * @author Andrey Minov
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class FlightsEngineTest {

  @Mock
  private FlightsService flightsService;
  @Mock
  private RoutesEngine routesEngine;

  private FlightsEngine flightsEngine;

  @Before
  public void setUp() throws Exception {
    flightsEngine = new ServicesFlightsEngine(flightsService, routesEngine);
  }

  @Test
  public void correctDirectFlight() {
    FlightRoute direct = mock(FlightRoute.class);
    Airport departure = mock(Airport.class);
    when(departure.getTimeZone()).thenReturn("Europe/Kiev");
    Airport arrival = mock(Airport.class);
    when(arrival.getTimeZone()).thenReturn("Europe/Amsterdam");

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

    Collection<List<Flight>> flights = flightsEngine.searchFlights(departure, arrival, date, 1, 0, 10);
    assertEquals(singletonList(singletonList(flight)), flights);
  }
}