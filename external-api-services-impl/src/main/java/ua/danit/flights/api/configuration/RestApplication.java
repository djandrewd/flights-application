package ua.danit.flights.api.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.engine.impl.configuration.EnginesConfiguration;

/**
 * Starting point for external API rest application.
 *
 * @author Andrey Minov
 */
public class RestApplication extends AbstractAnnotationConfigDispatcherServletInitializer {
  private static final String EXTERNAL_API_SERVICES = "/api/*";

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {PropertiesConfiguration.class, DataConfiguration.class,
        EnginesConfiguration.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[] {RestApplicationConfiguration.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {EXTERNAL_API_SERVICES};
  }
}
