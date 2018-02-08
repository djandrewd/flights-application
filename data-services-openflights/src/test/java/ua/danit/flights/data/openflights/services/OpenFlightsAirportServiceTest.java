package ua.danit.flights.data.openflights.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.danit.flights.data.model.Airport;

/**
 * Test for OpenData airport service.
 *
 * @author Andrey Minov
 */
public class OpenFlightsAirportServiceTest {
  private OpenFlightsAirportService airportService;

  @Before
  public void setUp() throws Exception {
    airportService = new OpenFlightsAirportService();
  }

  @Test
  public void testBoryspil() {
    Airport airport = airportService.getByIata("KBP").get(0);
    assertNotNull(airport);
    Assert.assertEquals("Kiev", airport.getCity());
  }

  @Test
  public void testSchiphol() {
    Airport airport = airportService.getByIata("AMS").get(0);
    assertNotNull(airport);
    Assert.assertEquals("Amsterdam", airport.getCity());
  }
}