package ua.danit.flights.data.impl.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.data.impl.dao.AirportsDao;
import ua.danit.flights.data.impl.utils.Utils;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.openflights.services.OpenFlightsAirportService;
import ua.danit.flights.data.services.AirportService;

/**
 * Test for data airline service loading.
 *
 * @author Andrey Minov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataConfiguration.class})
public class DataAirportServiceTest {

  private static AirportService openFlightsService;

  @Autowired
  private AirportService airportService;

  @Autowired
  private AirportsDao airportsDao;

  @BeforeClass
  public static void externalServiceInit() {
    openFlightsService = new OpenFlightsAirportService();
  }

  @Before
  public void setUp() throws Exception {
    List<Airport> airports = openFlightsService.getByIata("KBP");
    airports.stream().map(Utils.airportConvert()).forEach(airportsDao::save);
  }

  @Test
  public void testLoadOfAirport() {
    List<Airport> airports = airportService.getByIata("KBP");
    assertNotNull(airports);
    assertFalse(airports.isEmpty());

    Airport probe = airports.get(0);
    assertEquals("KBP", probe.getIata());
    assertEquals("Kiev", probe.getCity());
    assertEquals(2939, probe.getId());
  }

  @Test
  public void testLoadOfAirportById() {
    Optional<Airport> airport = airportService.getById(2939);
    assertTrue(airport.isPresent());
    Airport probe = airport.get();
    assertEquals("KBP", probe.getIata());
    assertEquals("Kiev", probe.getCity());
    assertEquals(2939, probe.getId());

  }

  @Test
  public void testMatchStart() {
    List<Airport> airports = airportService.findByCity("Kie");
    assertNotNull(airports);
    assertFalse(airports.isEmpty());

    Airport probe = airports.get(0);
    assertEquals("KBP", probe.getIata());
    assertEquals("Kiev", probe.getCity());
    assertEquals(2939, probe.getId());
  }

  @Test
  public void testMatchEnd() {
    List<Airport> airports = airportService.findByCity("iev");
    assertNotNull(airports);
    assertFalse(airports.isEmpty());

    Airport probe = airports.get(0);
    assertEquals("KBP", probe.getIata());
    assertEquals("Kiev", probe.getCity());
    assertEquals(2939, probe.getId());
  }

  @Test
  public void testIaiaOfAirport() {
    List<Airport> airports = airportService.findByIata("KB");
    assertNotNull(airports);
    assertFalse(airports.isEmpty());

    Airport probe = airports.get(0);
    assertEquals("KBP", probe.getIata());
    assertEquals("Kiev", probe.getCity());
    assertEquals(2939, probe.getId());
  }

}