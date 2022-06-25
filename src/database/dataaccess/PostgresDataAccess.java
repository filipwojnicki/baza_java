package database.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.provider.Postgres;
import model.Model;

public final class PostgresDataAccess implements DataAccess {
  private static Logger logger = Logger.getLogger(PostgresDataAccess.class.getName());
  private Connection connection = null;
  private String schema = null;

  public PostgresDataAccess() throws SQLException {
    this.openConnection();
  }

  public List<Map<String, String>> fetchOne(Model model, String... condition) throws SQLException {
    String conditionTemp = "";

    if(condition.length > 0) {
      conditionTemp = convertStrArrayToString(condition) + " LIMIT 1";
    } else {
      conditionTemp = "true LIMIT 1";
    }

    return this.fetchAll(model, conditionTemp);
  }

  public List<Map<String, String>> fetchAll(Model model, String... condition) throws SQLException {
    Statement statement = null;
    List<Map<String, String>> results = new ArrayList<>();

    if (connection == null) {
      logger.log(Level.WARNING, "Connection not found");
      return results;
    }
    
    try {
      statement = connection.createStatement();

      String query = "Select ";
      String[] fields = model.getAttributes();

      for(int i = 0; i < fields.length; i++) {
        String separator = i < fields.length - 1 ? ", " : " ";
        query += fields[i] + separator;
      }

      query += "from " + schema + getModelName(model);

      if(condition.length > 0) {
        query += " WHERE " + convertStrArrayToString(condition);
      }

      logger.info(query);

      ResultSet rs = statement.executeQuery(query);

      int index = 0;
      while (rs.next()) {
        Map<String, String> record = new HashMap<String, String>();        

        for(int i = 0; i < fields.length; i++) {
          String column = fields[i];
          String value = rs.getString(column);
          record.put(column, value);
        }
        results.add(index, record);
        index++;
      }

      rs.close();
    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
    return results;
  }

  public void create(Model model) throws SQLException {
    PreparedStatement ps = null;

    if (connection == null) {
      logger.log(Level.WARNING, "Connection not found");
      return;
    }

    String query = "INSERT INTO " + schema + getModelName(model) + "( ";
    String[] fields = model.getAttributes();
    Map<String, String> values = model.getValues();

    String indexValue = model.getIndexField();

    values.remove(indexValue);

    List<String> list = new ArrayList<String>(Arrays.asList(fields));
    list.remove(indexValue);

    String[] fieldsWithoutIndexField = list.toArray(new String[0]);

    for(int i = 0; i < fieldsWithoutIndexField.length; i++) {
      String separator = i < fieldsWithoutIndexField.length - 1 ? ", " : " ";
      query += fieldsWithoutIndexField[i] + separator;
    }

    query += ") VALUES (";

    for(int i = 0; i < fieldsWithoutIndexField.length; i++) {
      String separator = i < fieldsWithoutIndexField.length - 1 ? ", " : " ";
      query += "?" + separator;
    }

    query += ")";

    logger.info(query);
    logger.info(model.toString());

    try {
      ps = connection.prepareStatement(query);

      for(int i = 0; i < fieldsWithoutIndexField.length; i++) {
        System.out.println(i);
        String value = values.get(fieldsWithoutIndexField[i]);
        int preparedIndex = i + 1;
        if(isInteger(value)) {
          ps.setInt(preparedIndex, Integer.parseInt(value));
        } else {
          ps.setString(preparedIndex, value);
        }
      }

      System.out.println(ps.toString());

      ps.executeUpdate();
    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
    } finally {
      if(ps != null) {
        ps.close();
      }
    }
  }

  public boolean update(Model model) throws SQLException {
    boolean isUpdated = false;
    PreparedStatement ps = null;

    if (connection == null) {
      logger.log(Level.WARNING, "Connection not found");
      return isUpdated;
    }

    String query = "UPDATE " + schema + getModelName(model);
    String[] fields = model.getAttributes();
    Map<String, String> values = model.getValues();

    query += " SET ";

    for(int i = 0; i < fields.length; i++) {
      String separator = i < fields.length - 1 ? ", " : " ";
      query += fields[i] + " = ?" + separator;
    }

    query += " WHERE " + model.getIndexField() + " = " + values.get(model.getIndexField());

    logger.info(query);

    try {
      ps = connection.prepareStatement(query);

      for(int i = 1; i <= fields.length; i++) {
        String value = values.get(fields[i - 1]);
        if(isInteger(value)) {
          ps.setInt(i, Integer.parseInt(value));
        } else {
          ps.setString(i, value);
        }
      }

      isUpdated = ps.executeUpdate() == 0 ? false : true; 
    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
    } finally {
      if(ps != null) {
        ps.close();
      }
    }

    return isUpdated;
  }

  public void remove(Model model) throws SQLException {
    Statement statement = null;

    if (connection == null) {
      logger.log(Level.WARNING, "Connection not found");
      return;
    }
    
    try {
      statement = connection.createStatement();
      Map<String, String> values = model.getValues();

      String query = "DELETE from " + schema + getModelName(model);
      query += " WHERE " + model.getIndexField() + " = '" + values.get(model.getIndexField()) + "'";

      logger.info(query);

      statement.executeUpdate(query);
    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
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
    this.schema = connection.getSchema();
  }

  private String convertStrArrayToString(String[] arr) {
    return Arrays.toString(arr).replaceAll("[\\[\\]\\(\\)]", "");
  }

  public static boolean isInteger(String s) {
    try { 
        Integer.parseInt(s); 
    } catch(NumberFormatException e) { 
        return false; 
    } catch(NullPointerException e) {
        return false;
    }
    return true;
  }

  public String getModelName(Model model) {
    String modelName = model.getClass().getSimpleName();
    return '"' + modelName.substring(0, 1).toUpperCase() + modelName.substring(1) + '"';
  }
}