package ua.danit.flights.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApplicationExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

  @ExceptionHandler(value = {Exception.class})
  protected ModelAndView handleException(Exception ex, WebRequest request) {
    LOGGER.error("Error occurred during processing entries: " + ex, ex);
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("flights-title");
    modelAndView.addObject("error", "Server error occurred!");
    return modelAndView;
  }
}