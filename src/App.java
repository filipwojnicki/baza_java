import java.util.Arrays;

import database.dataaccess.PostgresDataAccess;
import views.Cmd;


public class App {
    private static String[] views = null;

    public static void main(String[] args) throws Exception {
        Cmd cmd = new Cmd();

        views = cmd.getViews();
        int indexOfMainView = Arrays.asList(views).indexOf("Home.txt");

        cmd.showView(views[indexOfMainView]);

        PostgresDataAccess dataAccess = new PostgresDataAccess();  
        dataAccess.fetch("select ranga from firma.kadry.oddzialy limit 1;", "ranga");
    }
}
