import ORM.assegnazioneAutomatica;
import BusinessLogic.menuManager;
import ORM.*;

import java.util.Scanner;

    public class Main {
        public static void main(String[] args) {

            DatabaseConnection dbConnection = new DatabaseConnection();
            dbConnection.connectToDatabase();

            Scanner scanner = new Scanner(System.in);

            menuManager menuManager = new menuManager(dbConnection.getConnection());
            volontariManager volontariManager = new volontariManager(dbConnection.getConnection());
            mezziManager mezziManager = new mezziManager(dbConnection.getConnection());
            serviziManager serviziManager = new serviziManager(dbConnection.getConnection());
            assegnazioneAutomatica assegnazioneAutomatica = new assegnazioneAutomatica(dbConnection.getConnection());
            notificheManager notificheManager = new notificheManager(dbConnection.getConnection());
            pazientiManager pazientiManager = new pazientiManager(dbConnection.getConnection());


            // Rimuovi le disponibilità scadute e le notifiche lette
            serviziManager.rimuoviDisponibilitaScadute();
            notificheManager.eliminaNotificheLette();

            // Rimuovi i servizi scaduti
            serviziManager.rimuoviServiziScaduti();

            // Avvia gestionale
            menuManager.menuIniziale(scanner);

            scanner.close();
        }
    }
