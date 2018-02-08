package ua.danit.flights.data.impl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.danit.flights.data.impl.model.AirportConnection;
import ua.danit.flights.data.impl.model.ConnectionRoute;

/**
 * Data access object for storing calculated results for
 * {@link ua.danit.flights.data.impl.model.AirportConnection} object.
 *
 * @author Andrey Minov
 */
@Repository
public class AirportConnectionStoreDao {

  private static final String INSERT_ROUTE_SQL =
      "INSERT INTO CONNECTION_ROUTES(FLIGHT_ROUTE_ID, AIRPORT_CONNECTION_ID, POSITION)"
      + " VALUES (?, ?, ?)";
  private static final String INSERT_CONNECTION_SQL =
      "INSERT INTO AIRPORT_CONNECTION(DEPARTURE_AIRPORT_ID, ARRIVAL_AIRPORT_ID, STOPS)"
      + " VALUES (?, ?, ?)";


  private final DataSource dataSource;

  /**
   * Instantiates a new Airport connection store dao.
   *
   * @param dataSource the data source
   */
  @Autowired
  public AirportConnectionStoreDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Transactional saves list of entries into database using list JDBC.
   * This method should be used for storing huge amount of connection between airports.
   *
   * @param entities the collection of airport entries to be saved.
   */
  public void save(Collection<AirportConnection> entities) {
    try (Connection connection = dataSource.getConnection()) {
      try {
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection
            .prepareStatement(INSERT_CONNECTION_SQL, Statement.RETURN_GENERATED_KEYS)) {
          for (AirportConnection entity : entities) {
            statement.setLong(1, entity.getDeparture().getId());
            statement.setLong(2, entity.getArrival().getId());
            statement.setInt(3, entity.getRoutes().size());
            statement.addBatch();
          }
          statement.executeBatch();
          try (ResultSet resultSet = statement.getGeneratedKeys()) {
            Iterator<AirportConnection> iterator = entities.iterator();
            while (resultSet.next() && iterator.hasNext()) {
              long id = resultSet.getLong(1);
              AirportConnection entity = iterator.next();
              try (PreparedStatement routeStatement = connection
                  .prepareStatement(INSERT_ROUTE_SQL)) {
                for (ConnectionRoute route : entity.getRoutes()) {
                  routeStatement.setLong(1, route.getFlightRoute().getId());
                  routeStatement.setLong(2, id);
                  routeStatement.setInt(3, route.getPosition());
                  routeStatement.addBatch();
                }
                routeStatement.executeBatch();
              }
            }
          }
        }
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        throw new RuntimeException("SQL exception occured: " + e.getMessage(), e);
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException("SQL exception occured: " + e.getMessage(), e);
    }
  }
}
