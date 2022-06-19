import java.util.Arrays;
import java.util.List;
import java.util.Map;

import database.dataaccess.PostgresDataAccess;
import views.Cmd;

import model.Ceny;


public class App {
    private static String[] views = null;

    public static void main(String[] args) throws Exception {
        Cmd cmd = new Cmd();
        Ceny ceny = new Ceny();

        views = cmd.getViews();
        int indexOfMainView = Arrays.asList(views).indexOf("Home.txt");

        cmd.showView(views[indexOfMainView]);

        PostgresDataAccess dataAccess = new PostgresDataAccess();  

        List<Map<String, String>> results = dataAccess.fetchAll(ceny);

        for(int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i));
        }

        Ceny cena = new Ceny();
        cena.nazw_towaru = results.get(0).get("nazw_towaru");
        cena.cena = results.get(0).get("cena");
        // cena.nazw_towaru = "mleko";
        // cena.cena = "1";

        System.out.println(cena.toString());
        // dataAccess.update(cena);
        // dataAccess.remove(cena);
        // dataAccess.create(cena);
        
    }
}
