import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

public class menuManager {
    private static Connection connection;
    public menuManager(Connection connection) {
        this.connection = connection;
    }


    //MENU INIZIALE
    public static void menuIniziale(Scanner scanner) {

        boolean sceltaValida = false;

        System.out.println("Benvenuto al Gestionale Turni!");
        while (!sceltaValida) {
            System.out.println("Vuoi fare l'accesso o registrarti o uscire? (A/R/U)");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("R")) {
                // Chiamata al metodo di registrazione
                sceltaValida = true;
                volontariManager.registrazione(scanner, sceltaValida);
            } else if (choice.equalsIgnoreCase("A")) {
                // Chiamata al metodo di accesso
                sceltaValida = true;
                volontariManager.accesso(scanner, sceltaValida);
            } else if (choice.equalsIgnoreCase("U")) {
                // Uscita dal programma
                System.out.println("Grazie per aver utilizzato il gestionale! Arrivederci.");
                System.exit(0);
            } else {
                System.out.println("Scelta non valida. Riprova.");
            }
        }

    }





    //MENU UTENTE
    public static void mostraMenuUtenteNormale(Scanner scanner, int matricolaVolontario) {
        System.out.println("Menu:");
        System.out.println("1. Dai disponibilità");
        System.out.println("2. Rimuovi disponibilità");
        System.out.println("3. Visualizza servizi assegnati");
        System.out.println("4. Esci");
        System.out.println(" ");
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuma la nuova riga dopo nextInt()

        switch (choice) {
            case 1:
                // Esegui l'azione per dare una disponibilità
                volontariManager.inserisciDisponibilita(scanner, matricolaVolontario);
                break;
            case 2:
                // Esegui l'azione per rimuovere una disponibilità
                volontariManager.rimuoviDisponibilita(scanner, matricolaVolontario);
                break;
            case 3:
                // Uscire dal menu
                volontariManager.visualizzaServiziAssegnati(scanner, matricolaVolontario);
                break;
            case 4:
                // Uscire dal menu
                menuIniziale(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
                mostraMenuUtenteNormale(scanner, matricolaVolontario);
        }
    }

    //MENU ADMIN
    public static void mostraMenuAdmin(Scanner scanner) {
        System.out.println("Menu Amministratore:");
        System.out.println("1. Gestisci mezzi");
        System.out.println("2. Gestisci volontari");
        System.out.println("3. Gestisci servizi");
        System.out.println("4. Esci");
        System.out.println(" ");
        System.out.println("Seleziona un'opzione:");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuma la nuova riga dopo nextInt()

        switch (choice) {
            case 1:
                // Esegui l'azione per gestire mezzi
                mostraMenuMezzi(scanner);
                break;
            case 2:
                // Esegui l'azione per gestire volontari
                mostraMenuVolontari(scanner);
                break;
            case 3:
                // Esegui l'azione per gestire servizi
                mostraMenuServizi(scanner);
                break;
            case 4:
                // Uscire dal menu
                menuIniziale(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }

    //MENU MEZZI
    public static void mostraMenuMezzi(Scanner scanner) {
        System.out.println("Menu Gestione Mezzi:");
        System.out.println("1. Aggiungi mezzo");
        System.out.println("2. Modifica mezzo");
        System.out.println("3. Elimina mezzo");
        System.out.println("4. Torna al menu amministratore");
        System.out.println("Seleziona un'opzione:");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuma la nuova riga dopo nextInt()

        switch (choice) {
            case 1:
                // Esegui l'azione per aggiungere un mezzo
                mezziManager.aggiungiMezzo(scanner);
                break;
            case 2:
                // Esegui l'azione per modificare un mezzo
                mezziManager.modificaMezzo(scanner);
                break;
            case 3:
                // Esegui l'azione per eliminare un mezzo
                mezziManager.eliminaMezzo(scanner);
                break;
            case 4:
                // Torna al menu amministratore
                mostraMenuAdmin(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }

    //MENU VOLONTARI
    public static void mostraMenuVolontari(Scanner scanner){
        System.out.println("Menu Gestione Volontari:");
        System.out.println("1. Modifica anagrafe volontari");
        System.out.println("2. Elimina volontari");
        System.out.println("3. Torna al menu amministratore");
        System.out.println("Seleziona un'opzione:");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuma la nuova riga dopo nextInt()

        switch (choice) {
            case 1:
                // Esegui l'azione per modificare l'anagrafe di un volontario
                volontariManager.modificaAnagrafeVolontari(scanner);
                break;
            case 2:
                // Esegui l'azione per eliminare un volontario
                volontariManager.eliminaVolontario(scanner);
                break;
            case 3:
                // Torna al menu amministratore
                mostraMenuAdmin(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }



    //MENU SERVIZI
    public static void mostraMenuServizi(Scanner scanner) {
        System.out.println("Menu Gestione Servizi:");
        System.out.println("1. Aggiungi servizio");
        System.out.println("2. Elimina servizio");
        System.out.println("3. Modifica dati servizio");
        System.out.println("4. Assegna automaticamente i volontari");
        System.out.println("5. Torna al menu amministratore");
        System.out.println("Seleziona un'opzione:");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuma la nuova riga dopo nextInt()

        switch (choice) {
            case 1:
                // Esegui l'azione per aggiungere un servizio
                serviziManager.aggiungiServizio(scanner);
                break;
            case 2:
                // Esegui l'azione per eliminare un servizio
                serviziManager.eliminaServizio(scanner);
                break;
            case 3:
                serviziManager.modificaServizio(scanner);
                break;
            case 4:
                // Torna al menu amministratore
                assegnazioneAutomatica.assegnaAutomaticamente();
                break;
            case 5:
                // Torna al menu amministratore
                mostraMenuAdmin(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }


}

