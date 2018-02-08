package ua.danit.flights.web.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import ua.danit.flights.data.impl.configuration.DataConfiguration;
import ua.danit.flights.engine.impl.configuration.EnginesConfiguration;

public class WebApplication extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {PropertiesConfiguration.class, DataConfiguration.class,
        EnginesConfiguration.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[] {WebApplicationConfiguration.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }
}
