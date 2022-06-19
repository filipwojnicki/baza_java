package model;

import java.util.HashMap;
import java.util.Map;

public class Ceny extends Model {
  public String nazw_towaru;
  public String cena;

  public String indexField = "nazw_towaru";
  private String[] attributes = {"nazw_towaru", "cena"};

  @Override
  public String toString() {
    return nazw_towaru + ", " + cena;
  }

  @Override
  public String getIndexField() {
    return indexField;
  }

  @Override
  public String[] getAttributes() {
    return attributes;
  }

  @Override
  public Map<String, String> getValues() {
    Map<String, String> values = new HashMap<String, String>();        

    values.put("nazw_towaru", nazw_towaru);
    values.put("cena", cena);

    return values;
  }
}
