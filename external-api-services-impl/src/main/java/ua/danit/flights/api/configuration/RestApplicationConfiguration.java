package ua.danit.flights.api.configuration;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_ZONE_ID;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * String application configuration for REST external API configuration.
 *
 * @author Andrey Minov
 */
@Configuration
@EnableWebMvc
@ComponentScan("ua.danit.flights.api.resources")
public class RestApplicationConfiguration extends WebMvcConfigurerAdapter {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    Jackson2ObjectMapperBuilder builder;
    builder = new Jackson2ObjectMapperBuilder().indentOutput(true)
                                               .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
                                               .featuresToEnable(WRITE_DATES_WITH_ZONE_ID);
    converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
  }
}
