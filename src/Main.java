import BusinessLogic.menuController;
import ORM.*;

import java.util.Scanner;

    public class Main {
        public static void main(String[] args) {

            DatabaseConnection dbConnection = new DatabaseConnection();
            dbConnection.connectToDatabase();

            Scanner scanner = new Scanner(System.in);

            menuController menuController = new menuController(dbConnection.getConnection());
            utenteDAO utenteDAO = new utenteDAO(dbConnection.getConnection());
            mezzoDAO mezzoDAO = new mezzoDAO(dbConnection.getConnection());
            servizioDAO servizioDAO = new servizioDAO(dbConnection.getConnection());
            notificaDAO notificaDAO = new notificaDAO(dbConnection.getConnection());
            pazienteDAO pazienteDAO = new pazienteDAO(dbConnection.getConnection());
            DisponibilitaDAO disponibilitaDAO = new DisponibilitaDAO(dbConnection.getConnection());


            // Rimuovi le disponibilità scadute e le notifiche lette
            servizioDAO.rimuoviDisponibilitaScadute();
            notificaDAO.eliminaNotificheLette();

            // Rimuovi i servizi scaduti
            servizioDAO.rimuoviServiziScaduti();

            // Avvia gestionale
            menuController.menuIniziale(scanner);

            scanner.close();
        }
    }
