package ua.danit.flights.api.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.danit.flights.api.configuration.RestApplicationConfiguration;
import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.data.impl.dao.AirlinesDao;
import ua.danit.flights.data.impl.dao.AirportsDao;
import ua.danit.flights.data.impl.dao.FlightRoutesDao;
import ua.danit.flights.data.impl.model.MappedAirline;
import ua.danit.flights.data.impl.model.MappedAirport;
import ua.danit.flights.data.impl.model.MappedFlightRoute;
import ua.danit.flights.data.impl.model.MappedPlane;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.engine.impl.configuration.EnginesConfiguration;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DataConfiguration.class, EnginesConfiguration.class,
    RestApplicationConfiguration.class})
public class FlightsResourceControllerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(FlightsResourceControllerTest.class);

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private AirportsDao airportsDao;

  @Autowired
  private AirportService airportService;

  @Autowired
  private AirlinesDao airlinesDao;

  @Autowired
  private FlightRoutesDao flightRoutesDao;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MappedAirport departure = getOrCreate("KBP", v -> {
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

    MappedAirport arrival = getOrCreate("AMS", v -> {
      MappedAirport airport = new MappedAirport();
      airport.setId(11);
      airport.setName("Amsterdam Airport Schiphol");
      airport.setIata("AMS");
      airport.setCity("Amsterdam");
      airport.setCountry("Netherlands");
      airport.setTimeZone("Europe/Amsterdam");
      airportsDao.save(airport);
      return airport;
    });

    MappedAirline airline = new MappedAirline();
    airline.setId(22);
    airline.setIata("KL");
    airline.setName("KLM Royal Dutch Airlines");
    airline.setCountry("Netherlands");
    try {
      airlinesDao.save(airline);
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
    }

    MappedPlane mappedPlane = new MappedPlane();
    mappedPlane.setTotalSeats(100);
    mappedPlane.setManufacturer("Boeing");
    mappedPlane.setType("ER-747");

    MappedFlightRoute route = new MappedFlightRoute();
    route.setId(1);
    route.setOperator(airline);
    route.setDeparture(departure);
    route.setArrival(arrival);
    route.setDepartureTime(LocalTime.of(5, 45));
    route.setArrivalTime(LocalTime.of(7, 35));
    route.setDailySchedule(127);
    route.setPlane(mappedPlane);
    //
    try {
      flightRoutesDao.save(route);
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
    }
  }

  @Test
  public void testDirectRouteSearch() throws Exception {
    String json = new String(
        Files.readAllBytes(Paths.get(getClass().getResource("/test_flights.json").toURI())),
        "UTF-8");

    mockMvc.perform(get("/flights/search").param("date", "2017-09-28").param("departure", "KBP")
                                          .param("arrival", "AMS").param("stops", "1"))
           .andExpect(status().isOk()).andExpect(content().json(json));
  }

  private MappedAirport getOrCreate(String iata, Function<String, MappedAirport> provider) {
    List<Airport> airports = airportService.getByIata(iata);
    if (airports.isEmpty()) {
      return provider.apply(iata);
    }
    return (MappedAirport) airports.get(0);
  }

}