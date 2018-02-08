package ua.danit.flights.api.resources;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests for airport REST resources.
 *
 * @author Andrey Minov
 */
@Ignore
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {})
public class AirportsResourceControllerTest {

  @Autowired
  private WebApplicationContext applicationContext;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    /*
     * Save airport data with information
      * {
      *      "iata": "KBP",
      *      "name": "Borispil",
      *      "city": "Kiev",
      *      "country": "Ukraine",
      *      "timeZone": "Europe/Kiev"
      *   }
     * */
  }

  @Test
  public void testAirportByCity() throws Exception {
    String response =
        "[{\n" + "\"iata\": \"KBP\",\n" + "\"name\": \"Borispil\",\n" + "\"city\": \"Kiev\",\n"
        + "\"country\": \"Ukraine\",\n" + "\"timeZone\": \"Europe/Kiev\"\n}]";
    mockMvc.perform(get("/airports/byCity").param("city", "Kiev")).andExpect(status().isOk())
           .andExpect(content().json(response));
  }

  @Test
  public void testAirportByIata() throws Exception {
    String response =
        "[{\n" + "\"iata\": \"KBP\",\n" + "\"name\": \"Borispil\",\n" + "\"city\": \"Kiev\",\n"
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


}