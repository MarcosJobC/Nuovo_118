import BusinessLogic.menuController;
import ORM.*;

import java.util.Scanner;

    public class Main {
        public static void main(String[] args) {

            DatabaseConnection dbConnection = new DatabaseConnection();
            dbConnection.connectToDatabase();

            Scanner scanner = new Scanner(System.in);

            // Rimuovi le disponibilità scadute e le notifiche lette
            DisponibilitaDAO.rimuoviDisponibilitaScaduteDAO();
            notificaDAO.eliminaNotificheLetteDAO();

            // Rimuovi i servizi scaduti
            servizioDAO.rimuoviServiziScadutiDAO();

            // Avvia gestionale
            menuController.menuIniziale(scanner);

            scanner.close();
        }
    }
