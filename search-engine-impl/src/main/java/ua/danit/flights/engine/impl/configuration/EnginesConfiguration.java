package ua.danit.flights.engine.impl.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ua.danit.flights.data.impl.services.DataAirportConnectionsService;
import ua.danit.flights.data.services.AirportService;
import ua.danit.flights.data.services.RoutesService;
import ua.danit.flights.engine.RoutesEngine;
import ua.danit.flights.engine.impl.engines.CachedServicesRouteEngine;
import ua.danit.flights.engine.impl.engines.DataServicesRouteEngine;

/**
 * Spring configuration for route and flight engines.
 *
 * @author Andrey Minov
 */
@Configuration
@ComponentScan("ua.danit.flights.engine.impl")
public class EnginesConfiguration {

  @Value("${engine.lazy:true}")
  private Boolean useLazyLoading;

  @Value("${engine.max_stops:3}")
  private Integer maxStops;

  @Value("${engine.init_on_start:false}")
  private Boolean initOnStart;

  /**
   * Create routes engine managed route.
   *
   * @param routesService             the routes data objects service
   * @param airportService            the airport data objects service
   * @param airportConnectionsService the airport connections data objects service
   * @param executorService           the executor service for calculation of routes.
   * @return the routes engine bean used for calculation of the routes.
   */
  @Bean
  public RoutesEngine routesEngine(RoutesService routesService, AirportService airportService,
                                   DataAirportConnectionsService airportConnectionsService,
                                   ExecutorService executorService) {
    return useLazyLoading ? new CachedServicesRouteEngine(routesService, airportService, maxStops,
        executorService, initOnStart)
        : new DataServicesRouteEngine(routesService, airportService, airportConnectionsService,
            executorService, maxStops, initOnStart);
  }

  /**
   * Scheduled executor service used inside route application.
   *
   * @return the scheduled executor service used in routes application.
   */
  @Bean
  public ScheduledExecutorService executorService() {
    return Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors());
  }
}
