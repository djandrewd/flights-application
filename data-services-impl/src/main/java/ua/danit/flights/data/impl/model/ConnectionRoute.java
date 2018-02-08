package ua.danit.flights.data.impl.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Holds connection route in the {@link AirportConnection} instance.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "CONNECTION_ROUTES")
public class ConnectionRoute implements Serializable {
  @Id
  @ManyToOne
  @JoinColumn(name = "FLIGHT_ROUTE_ID")
  private MappedFlightRoute flightRoute;

  @Id
  @ManyToOne
  @JoinColumn(name = "AIRPORT_CONNECTION_ID")
  private AirportConnection airportConnection;

  private int position;

  public MappedFlightRoute getFlightRoute() {
    return flightRoute;
  }

  public void setFlightRoute(MappedFlightRoute flightRoute) {
    this.flightRoute = flightRoute;
  }

  public AirportConnection getAirportConnection() {
    return airportConnection;
  }

  public void setAirportConnection(AirportConnection airportConnection) {
    this.airportConnection = airportConnection;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConnectionRoute route = (ConnectionRoute) o;

    if (position != route.position) {
      return false;
    }
    if (flightRoute != null ? !flightRoute.equals(route.flightRoute) : route.flightRoute != null) {
      return false;
    }
    return airportConnection != null ? airportConnection.equals(route.airportConnection)
        : route.airportConnection == null;
  }

  @Override
  public int hashCode() {
    int result = flightRoute != null ? flightRoute.hashCode() : 0;
    result = 31 * result + (airportConnection != null ? airportConnection.hashCode() : 0);
    result = 31 * result + position;
    return result;
  }
}
