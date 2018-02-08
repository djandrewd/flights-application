package ua.danit.flights.web.controllers;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.danit.flights.api.model.Airline;
import ua.danit.flights.api.model.FlightRoute;
import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.Flight;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.engine.FlightsEngine;

/**
 * Flight search controller for searching and rendering flights search results.
 *
 * @author Andrey Minov.
 */
@Controller
@RequestMapping("search")
public class FlightSearchController {
  private static final Logger LOGGER = LoggerFactory.getLogger(FlightSearchController.class);

  private static final String MDC_KEY = "process";
  private static final String MDC_PREFIX = "FSC_%s";

  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_MAX_ON_PAGE = 50;

  private final FlightsEngine flightsEngine;
  private final AirportService airportService;

  /**
   * Instantiates a new Flight search controller.
   *
   * @param flightsEngine  the flights engine for searching the flights
   * @param airportService the airport service for receiving information about airports.
   */
  @Autowired
  public FlightSearchController(FlightsEngine flightsEngine, AirportService airportService) {
    this.flightsEngine = flightsEngine;
    this.airportService = airportService;
  }

  /**
   * Search for flights with criteria provided by {@link SearchRequest} entry.
   *
   * @param searchRequest the search request as search criteria.
   * @param bindingResult the binding result for information about invalid requests.
   * @return the model and view for rendering search results.
   */
  @PostMapping
  public ModelAndView acceptRequest(@Valid SearchRequest searchRequest,
                                    BindingResult bindingResult) {
    MDC.put(MDC_KEY, format(MDC_PREFIX, UUID.randomUUID().toString()));
    try {
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.setViewName("flights-title");

      if (bindingResult.hasErrors()) {
        LOGGER.warn("Errors in input fields: {}", bindingResult.getAllErrors());
        FieldError fieldError = bindingResult.getFieldError();
        modelAndView
            .addObject("error", format("Incorrect field '%s' value!", fieldError.getField()));
      }

      if (!bindingResult.hasErrors()) {
        searchFlights(modelAndView, searchRequest);
      }
      return modelAndView;
    } finally {
      MDC.remove(MDC_KEY);
    }
  }

  private void searchFlights(ModelAndView modelAndView, SearchRequest searchRequest) {
    LOGGER.debug("Receive search request for date [{}] from [{}] to [{}]. Stops {}",
        searchRequest.getDate(), searchRequest.getDeparture(), searchRequest.getArrival(),
        searchRequest.getStops());
    List<Airport> opt = airportService.getByIata(searchRequest.getDeparture());
    if (opt.isEmpty()) {
      modelAndView.addObject("error", "Incorrect departure airport");
      return;
    }
    ua.danit.flights.data.model.Airport departure = opt.get(0);
    opt = airportService.getByIata(searchRequest.getArrival());
    if (opt.isEmpty()) {
      modelAndView.addObject("error", "Incorrect arrival airport");
      return;
    }
    ua.danit.flights.data.model.Airport arrival = opt.get(0);
    Collection<List<Flight>> flights = flightsEngine
        .searchFlights(departure, arrival, searchRequest.getDate(), searchRequest.getStops(),
            DEFAULT_PAGE, DEFAULT_MAX_ON_PAGE);
    LOGGER.debug("Found {} flights matching the criteria on page.", flights.size());
    modelAndView.addObject("flights", convert(departure, arrival, flights));
  }

  private ua.danit.flights.api.model.Airport fromAirport(
      ua.danit.flights.data.model.Airport airport) {
    return new ua.danit.flights.api.model.Airport(airport.getIata(), airport.getName(),
        airport.getCity(), airport.getCountry(), airport.getTimeZone());
  }

  private Airline fromAirline(ua.danit.flights.data.model.Airline airline) {
    return new Airline(airline.getName(), airline.getIata(), airline.getCountry());
  }

  private List<FlightRoute> convert(Airport departureAirport, Airport arrivalAirport,
                                    Collection<List<ua.danit.flights.data.model.Flight>> flights) {
    List<FlightRoute> out = new ArrayList<>(flights.size());
    for (List<ua.danit.flights.data.model.Flight> travelRoute : flights) {
      ZonedDateTime routeDepartureTime = null;
      ZonedDateTime routeArrivalTime = null;

      List<FlightRoute.Flight> transports = new ArrayList<>(travelRoute.size());
      for (ua.danit.flights.data.model.Flight flight : travelRoute) {
        ua.danit.flights.data.model.FlightRoute flightRoute = flight.getRoute();
        ua.danit.flights.data.model.Airport departure = flightRoute.getDeparture();
        ua.danit.flights.data.model.Airport arrival = flightRoute.getArrival();

        ZonedDateTime departureTime = null;
        if (flightRoute.getDepartureTime() != null) {
          departureTime = flight.getDate().atTime(flightRoute.getDepartureTime())
                                .atZone(ZoneId.of(departure.getTimeZone()));
        }
        routeDepartureTime = routeDepartureTime != null ? routeDepartureTime : departureTime;

        ZonedDateTime arrivalTime = null;
        if (flightRoute.getDepartureTime() != null) {
          arrivalTime = flight.getDate().atTime(flightRoute.getArrivalTime())
                              .atZone(ZoneId.of(arrival.getTimeZone()));
          if (departureTime != null && departureTime.isAfter(arrivalTime)) {
            arrivalTime = arrivalTime.plusDays(1);
          }
        }
        routeArrivalTime = arrivalTime;

        FlightRoute.Flight transport =
            new FlightRoute.Flight(flight.getRoute().getName(), fromAirport(departure),
                fromAirport(arrival), fromAirline(flightRoute.getOperator()), departureTime,
                arrivalTime);
        transports.add(transport);
      }

      out.add(new FlightRoute(fromAirport(departureAirport), fromAirport(arrivalAirport),
          routeDepartureTime, routeArrivalTime, BigDecimal.ZERO, transports));
    }
    return out;
  }
}
