package ua.danit.flights.engine.impl;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ua.danit.flights.data.model.Airport;
import ua.danit.flights.data.model.FlightRoute;
import ua.danit.flights.data.openflights.model.OpenFlightsAirport;
import ua.danit.flights.data.openflights.services.OpenFlightsAirlinesService;
import ua.danit.flights.data.openflights.services.OpenFlightsAirportService;
import ua.danit.flights.data.openflights.services.OpenFlightsRoutesService;
import ua.danit.flights.data.services.AirlineService;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;
import ua.danit.flights.engine.RoutesEngine;
import ua.danit.flights.engine.impl.engines.CachedServicesRouteEngine;

public class RoutesEngineSample {
    private static final String[] AIRPORTS_INTERESTS = {"IEV", "KBP"};

    public static void main(String[] args) {
        AirlineService airlineService = new OpenFlightsAirlinesService();
        AirportService airportService = new OpenFlightsAirportService();
        RoutesService routesService = new OpenFlightsRoutesService(airlineService, airportService);
        //
        //
        RoutesEngine routesEngine = new CachedServicesRouteEngine(routesService, airportService);
        System.out.println("Instantiating....");
        routesEngine.instantiate();
        System.out.println("Instantiating finished.");
        //

        Airport departure = airportService.findByIata("KBP").get(0);
        Airport arrival = airportService.findByIata("SYD").get(0);

        Collection<List<FlightRoute>> routes = routesEngine.findRoute(departure, arrival, 5, 1, 10);
        routes.forEach(System.out::println);
    }
}
