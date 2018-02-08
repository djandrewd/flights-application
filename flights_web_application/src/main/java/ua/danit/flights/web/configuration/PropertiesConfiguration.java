package ua.danit.flights.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Special Spring configuration to add property source before all other configurations.
 *
 * @author Andrey Minov
 */
@Configuration
@PropertySource("/WEB-INF/configuration/application.properties")
public class PropertiesConfiguration {
}
