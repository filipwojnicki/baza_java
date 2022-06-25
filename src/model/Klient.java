package model;

import java.util.HashMap;
import java.util.Map;

public class Klient extends Model {
  public String klient_id = null;
  public String imie;
  public String nazwisko;
  public String adres;
  public String telefon;

  public String indexField = "klient_id";
  private String[] attributes = {"klient_id", "imie", "nazwisko", "adres", "telefon"};

  @Override
  public String toString() {
    return klient_id + ", " + imie + ", " + nazwisko + ", " + adres + ", " + telefon;
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

    values.put("klient_id", klient_id);
    values.put("imie", imie);
    values.put("nazwisko", nazwisko);
    values.put("adres", adres);
    values.put("telefon", telefon);

    return values;
  }
}
