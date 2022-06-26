import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

        PostgresDataAccess dataAccess = new PostgresDataAccess(); 

        views = cmd.getViews();
        int indexOfMainView = Arrays.asList(views).indexOf("Menu.txt");
        int menuSelection = 0;

        cmd.clearConsole();
        cmd.showView(views[indexOfMainView]);

        while(true) {
            try {
                Scanner scan = new Scanner(System.in);
                menuSelection = scan.nextInt();

                switch(menuSelection) {
                    case 1:
                        cmd.clearConsole();
                        scan.nextLine();

                        List<Map<String, String>> products = dataAccess.fetchAll(produkt);

                        for(int i = 0; i < products.size(); i++) {
                            System.out.println(products.get(i));
                        }

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 2:
                        cmd.clearConsole();
                        scan.nextLine();

                        List<Map<String, String>> clients = dataAccess.fetchAll(klient);

                        for(int i = 0; i < clients.size(); i++) {
                            System.out.println(clients.get(i));
                        }
                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 3:
                        cmd.clearConsole();
                        scan.nextLine();

                        List<Map<String, String>> orders = dataAccess.fetchAll(zamowienie);

                        for(int i = 0; i < orders.size(); i++) {
                            System.out.println(orders.get(i));
                        }
                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 4:
                        cmd.clearConsole();
                        scan.nextLine();

                        List<Map<String, String>> positions = dataAccess.fetchAll(pozycja);

                        for(int i = 0; i < positions.size(); i++) {
                            System.out.println(positions.get(i));
                        }
                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 5:
                        cmd.clearConsole();
                        scan.nextLine();

                        Klient newClient = new Klient();
                        
                        System.out.println("Imie:");
                        newClient.imie = scan.nextLine();

                        System.out.println("Nazwisko:");
                        newClient.nazwisko = scan.nextLine();

                        System.out.println("Adres:");
                        newClient.adres = scan.nextLine();

                        System.out.println("Telefon:");
                        newClient.telefon = scan.nextLine();

                        dataAccess.create(newClient);

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 6:
                        cmd.clearConsole();
                        scan.nextLine();

                        Produkt newProduct = new Produkt();
                        
                        System.out.println("Nazwa:");
                        newProduct.nazwa = scan.nextLine();

                        System.out.println("Opis:");
                        newProduct.opis = scan.nextLine();

                        System.out.println("Cena:");
                        newProduct.cena = scan.nextLine();

                        System.out.println("Ilosc:");
                        newProduct.ilosc = scan.nextLine();

                        dataAccess.create(newProduct);

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 7:
                        cmd.clearConsole();
                        scan.nextLine();

                        Zamowienie order = new Zamowienie();

                        System.out.println("Klient id: ");
                        order.klient_id = scan.nextLine();

                        order.data = String.valueOf(new Timestamp(System.currentTimeMillis()));

                        int orderId = dataAccess.create(order);

                        if(orderId == 0) {
                            cmd.showView(views[indexOfMainView]);
                            break;
                        }

                        order.zamowienie_id = String.valueOf(orderId);

                        Pozycja position = new Pozycja();
                        position.zamowienie_id = order.zamowienie_id;

                        System.out.println("Produkt id: ");
                        position.produkt_id = scan.nextLine();

                        System.out.println("Ilosc: ");
                        position.ilosc = scan.nextLine();

                        int positionId = dataAccess.create(position);

                        if(positionId == 0) {
                            cmd.showView(views[indexOfMainView]);
                            break;
                        }

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 8:
                        cmd.clearConsole();
                        scan.nextLine();

                        Produkt product = new Produkt();

                        System.out.println("Produkt id: ");
                        product.produkt_id = scan.nextLine();

                        dataAccess.remove(product);

                        cmd.showView(views[indexOfMainView]);
                        break;
                    
                    case 9:
                        cmd.clearConsole();
                        scan.nextLine();

                        Klient client = new Klient();

                        System.out.println("Klient id: ");
                        client.klient_id = scan.nextLine();

                        dataAccess.remove(client);

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 10:
                        cmd.clearConsole();
                        scan.nextLine();

                        Zamowienie orderRemove = new Zamowienie();

                        System.out.println("Zamowienie id: ");
                        orderRemove.zamowienie_id = scan.nextLine();

                        dataAccess.remove(orderRemove);

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 11:
                        cmd.clearConsole();
                        scan.nextLine();

                        Pozycja positionRemove = new Pozycja();

                        System.out.println("Pozycja id: ");
                        positionRemove.pozycja_id = scan.nextLine();

                        dataAccess.remove(positionRemove);

                        cmd.showView(views[indexOfMainView]);
                        break;
                    
                    case 12:
                        cmd.clearConsole();
                        scan.nextLine();

                        boolean productIsUpdated = false;
                        Produkt productToUpdate = new Produkt();

                        System.out.println("Id produktu do zmiany: ");
                        productToUpdate.produkt_id = scan.nextLine();

                        String productIndexField = productToUpdate.getIndexField();
                        Map<String, String> productUpdateObject = dataAccess.fetchOne(productToUpdate, productIndexField + "=" + productToUpdate.produkt_id);
                        System.out.println(productUpdateObject);

                        String[] productAttributes = productToUpdate.getAttributes();
                        List<String> productAttrList= new ArrayList<String>(Arrays.asList(productAttributes));
                        productAttrList.remove(productIndexField);
                        String[] productFieldsWithoutIndexField = productAttrList.toArray(new String[0]);

                        for(int i = 0; i < productFieldsWithoutIndexField.length; i++) {
                            System.out.println(productFieldsWithoutIndexField[i] + ":");
                            String value = scan.nextLine();

                            if(value.isEmpty()) {
                                setField(productToUpdate, productFieldsWithoutIndexField[i], productUpdateObject.get(productFieldsWithoutIndexField[i]));
                                continue;
                            }

                            productUpdateObject.put(productFieldsWithoutIndexField[i], value);
                            setField(productToUpdate, productFieldsWithoutIndexField[i], value);
                            productIsUpdated = true;
                        }

                        System.out.println("Produkt: " + productToUpdate.toString());

                        if(productIsUpdated) {
                            dataAccess.update(productToUpdate);
                        }

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 13:
                        cmd.clearConsole();
                        scan.nextLine();

                        boolean clientIsUpdated = false;
                        Klient clientToUpdate = new Klient();

                        System.out.println("Id klient do zmiany: ");
                        clientToUpdate.klient_id = scan.nextLine();

                        String clientIndexField = clientToUpdate.getIndexField();
                        Map<String, String> clientUpdateObject = dataAccess.fetchOne(clientToUpdate, clientIndexField + "=" + clientToUpdate.klient_id);
                        System.out.println(clientUpdateObject);

                        String[] clientAttributes = clientToUpdate.getAttributes();
                        List<String> clientAttrList= new ArrayList<String>(Arrays.asList(clientAttributes));
                        clientAttrList.remove(clientIndexField);
                        String[] clientFieldsWithoutIndexField = clientAttrList.toArray(new String[0]);

                        for(int i = 0; i < clientFieldsWithoutIndexField.length; i++) {
                            System.out.println(clientFieldsWithoutIndexField[i] + ":");
                            String value = scan.nextLine();

                            if(value.isEmpty()) {
                                setField(clientToUpdate, clientFieldsWithoutIndexField[i], clientUpdateObject.get(clientFieldsWithoutIndexField[i]));
                                continue;
                            }

                            clientUpdateObject.put(clientFieldsWithoutIndexField[i], value);
                            setField(clientToUpdate, clientFieldsWithoutIndexField[i], value);
                            clientIsUpdated = true;
                        }

                        System.out.println("Klient: " + clientToUpdate.toString());

                        if(clientIsUpdated) {
                            dataAccess.update(clientToUpdate);
                        }

                        cmd.showView(views[indexOfMainView]);
                        break;

                    case 14:
                        cmd.clearConsole();
                        scan.nextLine();

                        boolean positionIsUpdated = false;
                        Pozycja positionToUpdate = new Pozycja();

                        System.out.println("Id pozycji do zmiany: ");
                        positionToUpdate.pozycja_id = scan.nextLine();

                        String positionIndexField = positionToUpdate.getIndexField();
                        Map<String, String> positionUpdateObject = dataAccess.fetchOne(positionToUpdate, positionIndexField + "=" + positionToUpdate.pozycja_id);
                        System.out.println(positionUpdateObject);

                        String[] positionAttributes = positionToUpdate.getAttributes();
                        List<String> positionAttrList= new ArrayList<String>(Arrays.asList(positionAttributes));
                        positionAttrList.remove(positionIndexField);
                        String[] positionFieldsWithoutIndexField = positionAttrList.toArray(new String[0]);

                        for(int i=0; i< positionFieldsWithoutIndexField.length; i++) {
                            System.out.println(positionFieldsWithoutIndexField[i] + ":");
                            String value = scan.nextLine();

                            if(value.isEmpty()) {
                                setField(positionToUpdate, positionFieldsWithoutIndexField[i], positionUpdateObject.get(positionFieldsWithoutIndexField[i]));
                                continue;
                            }

                            positionUpdateObject.put(positionFieldsWithoutIndexField[i], value);
                            setField(positionToUpdate, positionFieldsWithoutIndexField[i], value);
                            positionIsUpdated = true;
                        }

                        System.out.println("Pozycja: " + positionToUpdate.toString());

                        if(positionIsUpdated) {
                            dataAccess.update(positionToUpdate);
                        }

                        cmd.showView(views[indexOfMainView]);
                        break;
                }
            } catch (InputMismatchException e) {}
              catch (NoSuchElementException e) {}
        }
    }

    public static boolean setField(Object targetObject, String fieldName, Object fieldValue) {
        Field field;

        try {
            field = targetObject.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = null;
        }

        Class superClass = targetObject.getClass().getSuperclass();

        while (field == null && superClass != null) {
            try {
                field = superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
            }
        }

        if (field == null) {
            return false;
        }

        field.setAccessible(true);

        try {
            field.set(targetObject, fieldValue);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
