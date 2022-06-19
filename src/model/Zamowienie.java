package model;

import java.util.HashMap;
import java.util.Map;

public class Zamowienie extends Model {
  public String zamowienie_id;
  public String klient_id;
  public String data;

  public String indexField = "zamowienie_id";
  private String[] attributes = {"zamowienie_id", "klient_id", "data"};

  @Override
  public String toString() {
    return zamowienie_id + "" + klient_id + ", " + data;
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

    values.put("zamowienie_id", zamowienie_id);
    values.put("klient_id", klient_id);
    values.put("data", data);

    return values;
  }
}
