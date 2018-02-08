package ua.danit.flights.api.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(EntityExceptionHandler.class);

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleServerInternalException(Exception ex, WebRequest request) {
    LOGGER.error("Error occured during processing entries: " + ex, ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}