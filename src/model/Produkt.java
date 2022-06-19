package model;

import java.util.HashMap;
import java.util.Map;

public class Produkt extends Model {
  public String produkt_id;
  public String nazwa;
  public String opis;
  public String cena;
  public String ilosc;

  public String indexField = "produkt_id";
  private String[] attributes = {"produkt_id", "nazwa", "opis", "cena", "ilosc"};

  @Override
  public String toString() {
    return produkt_id + "" + nazwa + ", " + opis + ", " + cena + ", " + ilosc;
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

    values.put("produkt_id", produkt_id);
    values.put("nazwa", nazwa);
    values.put("opis", opis);
    values.put("cena", cena);
    values.put("ilosc", ilosc);

    return values;
  }
}
