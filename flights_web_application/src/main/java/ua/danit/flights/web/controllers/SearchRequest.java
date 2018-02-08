package ua.danit.flights.web.controllers;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Search request object filled by Spring from search request form.
 *
 * @author Andrey Minov
 */
@Valid
public class SearchRequest {
  @NotNull
  @NotEmpty
  private String departure;
  @NotNull
  @NotEmpty
  private String arrival;
  @DateTimeFormat(iso = DATE)
  @FutureOrPresent
  @NotNull
  private LocalDate date;
  @NotNull
  @Min(0)
  @Max(4)
  private Integer stops;

  String getDeparture() {
    return departure;
  }

  public void setDeparture(String departure) {
    this.departure = departure;
  }

  String getArrival() {
    return arrival;
  }

  public void setArrival(String arrival) {
    this.arrival = arrival;
  }

  LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  Integer getStops() {
    return stops;
  }

  public void setStops(Integer stops) {
    this.stops = stops;
  }
}
