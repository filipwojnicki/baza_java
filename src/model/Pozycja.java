package model;

import java.util.HashMap;
import java.util.Map;

public class Pozycja extends Model {
  public String pozycja_id;
  public String produkt_id;
  public String ilosc;
  public String zamowienie_id;

  public String indexField = "pozycja_id";
  private String[] attributes = {"pozycja_id", "produkt_id", "ilosc", "zamowienie_id"};

  @Override
  public String toString() {
    return pozycja_id + "," + produkt_id + "," + ilosc + "," + zamowienie_id;
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

    values.put("pozycja_id", pozycja_id);
    values.put("produkt_id", produkt_id);
    values.put("ilosc", ilosc);
    values.put("zamowienie_id", zamowienie_id);

    return values;
  }
}
