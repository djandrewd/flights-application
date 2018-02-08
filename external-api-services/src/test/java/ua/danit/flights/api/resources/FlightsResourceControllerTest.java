package ua.danit.flights.api.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

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

@Ignore
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {})
public class FlightsResourceControllerTest {

  @Autowired
  private WebApplicationContext applicationContext;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    /*
    * Here you should have story from single flight
    * connecting:
    * {
    *  "iata": "KBP",
    *  "name": "Boryspil International Airport",
    *  "city": "Kiev",
    *  "country": "Ukraine",
    *  "timeZone": "Europe/Kiev"
    *}
    * and
    * {
    *  "iata": "AMS",
    *  "name": "Amsterdam Airport Schiphol",
    *  "city": "Amsterdam",
    *  "country": "Netherlands",
    *  "timeZone": "Europe/Amsterdam"
    *}
    *
    * operated by
    * {
    *  "name": "KLM Royal Dutch Airlines",
    *  "iata": "KL",
    *  "country": "Netherlands"
    *}
    * which takes off on 2017-09-28 at 5:45 and
    * arrives at 2017-09-282 at 07:35.
    *
    * Check file test_flights.json
    *
    * */
  }

  @Test
  public void testDirectRouteSearch() throws Exception {
    String json = new String(
        Files.readAllBytes(Paths.get(getClass().getResource("test_flights.json").toURI())),
        "UTF-8");

    mockMvc.perform(get("/flights/search").param("date", "2017-09-28").param("departureIata", "IEV")
                                          .param("arrivalIata", "AMS").param("maxStops", "1"))
           .andExpect(status().isOk()).andExpect(content().json(json));
  }
}