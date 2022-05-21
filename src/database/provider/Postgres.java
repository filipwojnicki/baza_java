package database.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.Database;

public final class Postgres implements Database {
  private final String url = "jdbc:postgresql://localhost:5432/firma";
  private final String user = "postgres";
  private final String password = "admin";

  private Connection connection = null;
  private static Logger logger = Logger.getLogger(Postgres.class.getName());

  public Connection connect() {
      try {
        connection = DriverManager.getConnection(url, user, password);
        logger.log(Level.INFO, "Connected to the PostgreSQL server successfully.");
      } catch (SQLException e) {
        logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
        connection = null;
      }

      return connection;
  }

  public Connection getConnection() {
    return connection;
  }
}
