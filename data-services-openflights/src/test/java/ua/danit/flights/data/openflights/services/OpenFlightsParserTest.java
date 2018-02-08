package ua.danit.flights.data.openflights.services;

import static org.junit.Assert.assertEquals;
import static ua.danit.flights.data.openflights.utils.ParseUtils.parseAirport;

import org.junit.Test;
import ua.danit.flights.data.model.Airport;

/**
 * Test this implementation.
 *
 * @author Andrey Minov
 */
public class OpenFlightsParserTest {

  @Test
  public void testCorrectParse() {
    String line =
        "641,\"Harstad/Narvik Airport, Evenes\",\"Harstad/Narvik\",\"Norway\",\"EVE\",\"ENEV\""
        + ",68.491302490234,16.678100585938,84,1,\"E\",\"Europe/Oslo\",\"airport\",\"OurAirports\"";
    Airport airport = parseAirport(line);
    assertEquals(641, airport.getId());
    assertEquals("Harstad/Narvik Airport, Evenes", airport.getName());
    assertEquals("Harstad/Narvik", airport.getCity());
    assertEquals("Norway", airport.getCountry());
    assertEquals("EVE", airport.getIata());
    assertEquals("ENEV", airport.getIcao());
    assertEquals(68.491302490234, airport.getLatitude(), 0.0);
    assertEquals(16.678100585938, airport.getLongitude(), 0.0);
    assertEquals("Europe/Oslo", airport.getTimeZone());
  }

  @Test
  public void testCorrectNullParse() {
    String line = "57,\"Forestville Airport\",\"Forestville\",\"Canada\",\\N,\"CYFE\""
                  + ",48.74610137939453,-69.09719848632812,293,84,-5,\"A\",\"America/Toronto\",\"airport\",\"OurAirports\"";
    Airport airport = parseAirport(line);
    assertEquals(57, airport.getId());
    assertEquals("Forestville Airport", airport.getName());
    assertEquals("Forestville", airport.getCity());
    assertEquals("Canada", airport.getCountry());
    assertEquals(null, airport.getIata());
    assertEquals("CYFE", airport.getIcao());
    assertEquals(48.74610137939453, airport.getLatitude(), 0.0);
    assertEquals(-69.09719848632812, airport.getLongitude(), 0.0);
    assertEquals("America/Toronto", airport.getTimeZone());
  }

  @Test
  public void testContainingQuotes() {
    String line =
        "1502,\"Foggia \\\"Gino Lisa\\\" Airport\",\"Foggia\",\"Italy\",\"FOG\",\"LIBF\",41.432899,15.535,265,1,\"E\",\"Europe/Rome\",\"airport\",\"OurAirports\"";
    Airport airport = parseAirport(line);
    assertEquals(1502, airport.getId());
    assertEquals("Foggia \"Gino Lisa\" Airport", airport.getName());
  }

  @Test
  public void testMoreIncorrect() {
    String line =
        "11743,\"La Grande-4 Airport\",\"La Grande-4\",\"Canada\",\"YAH\",\"CYAH\",53.754699707,-73.6753005981,1005,\\N,\\N,\\N,\"airport\",\"OurAirports\"";
    Airport airport = parseAirport(line);
    assertEquals(11743, airport.getId());
    assertEquals(null, airport.getTimeZone());
  }
}