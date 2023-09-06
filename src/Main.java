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
        emegenzeManager emegenzeManager = new emegenzeManager(dbConnection.getConnection());
        TurniCentralinoManager TurniCentralinoManager = new TurniCentralinoManager(dbConnection.getConnection());




        // Aggiungi e rimuove le emergenze mancanti
        emegenzeManager.rimuoviEmergenzePassate();
        emegenzeManager.aggiungiEmergenzeMancanti();

        // Aggiungi e rimuove le emergenze mancanti
        TurniCentralinoManager.rimuoviTurniPassati();
        TurniCentralinoManager.aggiungiTurniMancanti();

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

