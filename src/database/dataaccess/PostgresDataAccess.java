package database.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.provider.Postgres;
import model.Model;

public final class PostgresDataAccess implements DataAccess {
  private static Logger logger = Logger.getLogger(PostgresDataAccess.class.getName());
  private Connection connection = null;
  private String schema = null;

  public PostgresDataAccess() throws SQLException {
    this.openConnection();
  }

  public Map<String, String> fetchOne(Model model, String... condition) throws SQLException {
    String conditionTemp = "";
    Map<String, String> result = new HashMap<String, String>(); 

    if(condition.length > 0) {
      conditionTemp = convertStrArrayToString(condition) + " LIMIT 1";
    } else {
      conditionTemp = "true LIMIT 1";
    }

    List<Map<String, String>> fetched = this.fetchAll(model, conditionTemp);

    if(fetched.size() > 0) {
      return fetched.get(0);
    }

    return result;
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

  public int create(Model model) throws SQLException {
    PreparedStatement ps = null;

    if (connection == null) {
      logger.log(Level.WARNING, "Connection not found");
      return 0;
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
      ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

      for(int i = 0; i < fieldsWithoutIndexField.length; i++) {
        String value = values.get(fieldsWithoutIndexField[i]);
        prepareStatementValues(ps, value, i);
      }

      if(ps.executeUpdate() == 0) {
        throw new SQLException("Data not inserted");
      }

      try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new SQLException("Insert failed, no ID obtained.");
        }
      }
    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
      return 0;
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

      for(int i = 0; i < fields.length; i++) {
        String value = values.get(fields[i]);
        prepareStatementValues(ps, value, i);
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

  public void executeFunction(String query, String... preparedAttributes) throws SQLException {
    PreparedStatement ps = null;
    logger.info(query);

    if (connection == null) {
      logger.log(Level.WARNING, "Connection not found");
      return;
    }
    
    try{
      ps = connection.prepareStatement(query);

      if(preparedAttributes.length > 0) {
        for(int i = 0; i < preparedAttributes.length; i++) {
          prepareStatementValues(ps, preparedAttributes[i], i);
        }
      }

      ResultSet rs = ps.executeQuery(); 

      while(rs.next()) {
        String result = rs.getString(getFunctionNameFromString(query));
        System.out.println(result);
      }

    } catch (SQLException e) {
      logger.log(Level.WARNING, "Driver not found. " + e.getMessage(), e);
    } finally {
      if(ps != null) {
        ps.close();
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

  public void prepareStatementValues(PreparedStatement ps, String value, int index) throws SQLException {
    int preparedIndex = index + 1;

    try{
      if(isInteger(value)) {
        ps.setInt(preparedIndex, Integer.parseInt(value));
      } else if(isFloat(value)) {
        ps.setFloat(preparedIndex, Float.parseFloat(value));
      } else if(isDouble(value)) {
        ps.setDouble(preparedIndex, Double.parseDouble(value));
      } else if(isTimestamp(value)) {
        ps.setTimestamp(preparedIndex, Timestamp.valueOf(value));
      } else {
        ps.setString(preparedIndex, value);
      }
    } catch (SQLException e) {
      System.out.println(value + " " + e);
    }
  }

  public String getModelName(Model model) {
    String modelName = model.getClass().getSimpleName();
    return '"' + modelName.substring(0, 1).toUpperCase() + modelName.substring(1) + '"';
  }

  public String getFunctionNameFromString(String text) {
    Pattern p = Pattern.compile("\\w+(?!\\s*\\w+)(?=\\s*\\w*\\((?!\\s*[\\w\\*]+\\s+[\\w\\*]+))");
    Matcher m = p.matcher(text.replaceAll("\"", ""));

    if (m.find()) {
      return m.group(0);
    }

    return "";
  }

  private String convertStrArrayToString(String[] arr) {
    return Arrays.toString(arr).replaceAll("[\\[\\]\\(\\)]", "");
  }

  public SimpleDateFormat getSqlTimestampSimpleDateFormat() {
    return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

  public static boolean isFloat(String s) {
    try{
      Float.parseFloat(s);
    } catch(NumberFormatException e) { 
      return false; 
    } catch(NullPointerException e) {
      return false;
    }
    return true;
  }

  public static boolean isDouble(String str) {
    try {
      Double.parseDouble(str);
    } catch(NumberFormatException e) { 
      return false; 
    } catch(NullPointerException e) {
      return false;
    }
    return true;
}

  public boolean isTimestamp(String timestamp) {
    SimpleDateFormat format = getSqlTimestampSimpleDateFormat();

    try {
      format.parse(timestamp);
      return true;
    } catch(ParseException e) {
      return false;
    }
  }
}