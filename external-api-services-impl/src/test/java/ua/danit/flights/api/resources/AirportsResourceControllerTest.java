package ua.danit.flights.api.resources;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.danit.flights.api.configuration.RestApplicationConfiguration;
import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.data.impl.dao.AirportsDao;
import ua.danit.flights.data.impl.model.MappedAirport;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.engine.impl.configuration.EnginesConfiguration;

/**
 * Tests for airport REST resources.
 *
 * @author Andrey Minov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataConfiguration.class, EnginesConfiguration.class,
    RestApplicationConfiguration.class})
public class AirportsResourceControllerTest {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private AirportsDao airportsDao;

  @Autowired
  private AirportService airportService;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    getOrCreate("KBP", v -> {
      MappedAirport airport = new MappedAirport();
      airport.setId(1);
      airport.setName("Boryspil International Airport");
      airport.setIata(v);
      airport.setCity("Kiev");
      airport.setCountry("Ukraine");
      airport.setTimeZone("Europe/Kiev");
      airportsDao.save(airport);
      return airport;
    });
  }

  @Test
  public void testAirportByCity() throws Exception {
    String response =
        "[{\n" + "\"iata\": \"KBP\",\n" + "\"name\": \"Boryspil International Airport\",\n" + "\"city\": \"Kiev\",\n"
        + "\"country\": \"Ukraine\",\n" + "\"timeZone\": \"Europe/Kiev\"\n}]";
    mockMvc.perform(get("/airports/byCity").param("city", "Kiev")).andExpect(status().isOk())
           .andExpect(content().json(response));
  }

  @Test
  public void testAirportByIata() throws Exception {
    String response =
        "[{\n" + "\"iata\": \"KBP\",\n" + "\"name\": \"Boryspil International Airport\",\n" + "\"city\": \"Kiev\",\n"
        + "\"country\": \"Ukraine\",\n" + "\"timeZone\": \"Europe/Kiev\"\n}]";
    mockMvc.perform(get("/airports/byIata").param("iata", "KBP")).andExpect(status().isOk())
           .andExpect(content().json(response));
  }

  @Test
  public void testIncorrectParam() throws Exception {
    mockMvc.perform(get("/airports/byIata")).andExpect(status().isBadRequest());
    mockMvc.perform(get("/airports/byCity")).andExpect(status().isBadRequest());
  }

  @Test
  public void testNothingFound() throws Exception {
    mockMvc.perform(get("/airports/byIata").param("iata", "AAAAAA")).andExpect(status().isOk())
           .andExpect(content().json("[]"));
  }

  private MappedAirport getOrCreate(String iata, Function<String, MappedAirport> provider) {
    List<Airport> airports = airportService.getByIata(iata);
    if (airports.isEmpty()) {
      return provider.apply(iata);
    }
    return (MappedAirport) airports.get(0);
  }


}