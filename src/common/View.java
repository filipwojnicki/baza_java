package common;

import java.io.IOException;

public interface View {
  public String currentView = "";

  void showView(String view);
  String[] getViews() throws IOException;
}
