package views;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.stream.Stream;

import common.View;

public class Cmd implements View {
  public String currentView = "";
  private final String path = "./src/views/cmd";

  public Cmd() {
    this.currentView = "Home";
  }

  public void showView(String view) {
    if(view == currentView) {
      return;
    }

    try (Stream<String> stream = Files.lines(Paths.get(this.path + "/" + view))) {
			stream.forEach(System.out::println);
      currentView = view;
		} catch (IOException e) {
			// e.printStackTrace();
      System.out.println("");
		}
  }

  public String[] getViews() throws IOException {
    ArrayList<String> list = new ArrayList<String>();

    try (Stream<Path> paths = Files.walk(Paths.get(this.path))) {
      paths
          .filter(Files::isRegularFile)
          .forEach(f -> list.add(f.getFileName().toString()));
      
          return (String[]) list.toArray(new String[0]);
    } catch (IOException e) {
      return null;
    }
  }

  public final void clearConsole() throws IOException, InterruptedException {
    // final String os = System.getProperty("os.name");

    // System.out.println(os);
    
    // if (os.contains("Windows")) {
    //   Runtime.getRuntime().exec("cls");
    // } else {
    //   Runtime.getRuntime().exec("clear");
    // }

    for (int i = 0; i < 50; i++) {
      System.out.println("");
    }
  }
}
