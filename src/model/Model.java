package model;

import java.util.Map;

public abstract class Model {
  public abstract String[] getAttributes();
  public abstract Map<String, String> getValues();
  public abstract String getIndexField();
}
