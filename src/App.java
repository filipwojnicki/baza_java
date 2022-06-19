import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import database.dataaccess.PostgresDataAccess;
import views.Cmd;

import model.Produkt;
import model.Klient;
import model.Pozycja;
import model.Zamowienie;

public class App {
    private static String[] views = null;

    public static void main(String[] args) throws Exception {
        Cmd cmd = new Cmd();
        Produkt produkt = new Produkt();
        Klient klient = new Klient();
        Pozycja pozycja = new Pozycja();
        Zamowienie zamowienie = new Zamowienie();

        Scanner scan = new Scanner(System.in);

        PostgresDataAccess dataAccess = new PostgresDataAccess(); 

        views = cmd.getViews();
        int indexOfMainView = Arrays.asList(views).indexOf("Home.txt");

        while(true) {
            cmd.showView(views[indexOfMainView]);

            int menuSelection = scan.nextInt();

            switch(menuSelection) {
                case 1:
                    cmd.clearConsole();
                    List<Map<String, String>> results = dataAccess.fetchAll(produkt);

                    for(int i = 0; i < results.size(); i++) {
                        System.out.println(results.get(i));
                    }
                    break;

                default:
                    cmd.clearConsole();
                    cmd.showView(views[indexOfMainView]);
                    break;
            }
        }

         

        

        // Ceny cena = new Ceny();
        // cena.nazw_towaru = results.get(0).get("nazw_towaru");
        // cena.cena = results.get(0).get("cena");
        // // cena.nazw_towaru = "mleko";
        // // cena.cena = "1";

        // System.out.println(cena.toString());
        // cmd.clearConsole();
        // dataAccess.update(cena);
        // dataAccess.remove(cena);
        // dataAccess.create(cena);
        
    }
}
