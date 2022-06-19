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
                    List<Map<String, String>> products = dataAccess.fetchAll(produkt);

                    for(int i = 0; i < products.size(); i++) {
                        System.out.println(products.get(i));
                    }
                    cmd.showView(views[indexOfMainView]);
                    break;

                case 2:
                    cmd.clearConsole();
                    List<Map<String, String>> clients = dataAccess.fetchAll(klient);

                    for(int i = 0; i < clients.size(); i++) {
                        System.out.println(clients.get(i));
                    }
                    cmd.showView(views[indexOfMainView]);
                    break;

                case 3:
                    cmd.clearConsole();
                    List<Map<String, String>> orders = dataAccess.fetchAll(zamowienie);

                    for(int i = 0; i < orders.size(); i++) {
                        System.out.println(orders.get(i));
                    }
                    cmd.showView(views[indexOfMainView]);
                    break;

                case 4:
                    cmd.clearConsole();
                    List<Map<String, String>> positions = dataAccess.fetchAll(pozycja);

                    for(int i = 0; i < positions.size(); i++) {
                        System.out.println(positions.get(i));
                    }
                    cmd.showView(views[indexOfMainView]);
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
