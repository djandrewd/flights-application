package ua.danit.flights.data.openflights.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import ua.danit.flights.data.model.Airline;
import ua.danit.flights.data.services.AirlineService;

/**
 * Test for OpenFlights service.
 *
 * @author Andrey Minov
 */
public class OpenFlightsAirlinesServiceTest {

  private AirlineService airlineService;

  @Before
  public void setUp() throws Exception {
    airlineService = new OpenFlightsAirlinesService();
  }

  @Test
  public void testExistedKLM() throws Exception {
    List<Airline> airline = airlineService.getByIata("KL");
    assertFalse(airline.isEmpty());
    assertEquals("KLM Royal Dutch Airlines", airline.get(0).getName());
  }


  @Test
  public void testExistedLH() throws Exception {
    Optional<Airline> airline = airlineService.getById(3320);
    assertTrue(airline.isPresent());
    assertEquals("Lufthansa", airline.get().getName());
  }

  @Test
  public void testExistedLHCode() throws Exception {
    List<Airline> airline = airlineService.getByIata("LH");
    assertFalse(airline.isEmpty());
    assertEquals("Lufthansa", airline.get(0).getName());
  }
}