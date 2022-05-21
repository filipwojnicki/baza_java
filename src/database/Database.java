package database;

import java.sql.Connection;

public interface Database {
  public Connection connect();
  public Connection getConnection();
}
