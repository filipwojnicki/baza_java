package database.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.provider.Postgres;

public final class PostgresDataAccess implements DataAccess {
  private static Logger logger = Logger.getLogger(PostgresDataAccess.class.getName());
  private Connection connection = null;
  
  public PostgresDataAccess() throws SQLException {
    this.connection = connection;
    this.openConnection();
  }

  public String fetch(String query, String columnName) throws SQLException {
    Statement statement = null;
    String resultMessage = "NO SELECT RESULT";

    if (connection == null) {
      return resultMessage;
    }
    
    try {
      statement = connection.createStatement();
      logger.info(query);

      ResultSet rs = statement.executeQuery(query);
      while (rs.next()) {
        logger.info("Data was found! " + rs.getString("ranga"));
      }
      rs.close();
    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
    return resultMessage;
  }

  private void openConnection() throws SQLException {
    Postgres connection = new Postgres();
    connection.connect();

    if(connection.getConnection() == null) {
      logger.warning("Can't connect to database");
      this.connection = null;
      return;
    }

    this.connection = connection.getConnection();
  }
}