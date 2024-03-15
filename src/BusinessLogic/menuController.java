package BusinessLogic;

import ORM.*;

import java.sql.Connection;
import java.util.Scanner;

public class menuController {
    private static Connection connection;
    public menuController(Connection connection) {
        this.connection = connection;
    }
    //MENU INIZIALE
    public static void menuIniziale(Scanner scanner) {

        boolean sceltaValida = false;

        System.out.println("Benvenuto al Gestionale Turni!");
        while (!sceltaValida) {
            System.out.print("Vuoi fare l'accesso o registrarti o uscire? (A/R/U): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("R")) {
                sceltaValida = true;
                VolontarioController.registrazione(scanner, sceltaValida);
            } else if (choice.equalsIgnoreCase("A")) {
                sceltaValida = true;
                VolontarioController.accesso(scanner, sceltaValida);
            } else if (choice.equalsIgnoreCase("U")) {
                System.out.println("Grazie per aver utilizzato il gestionale! Arrivederci.");
                System.exit(0);
            } else {
                System.out.println("Scelta non valida. Riprova.");
            }
        }

    }
    //MENU UTENTE
    public static void mostraMenuUtenteNormale(Scanner scanner, int matricolaVolontario) {
        System.out.println("MENU:");
        System.out.println("1. Dai disponibilità");

        // Verifica se l'utente ha delle disponibilità prima di visualizzare il punto 2
        if (utenteDAO.haDisponibilita(matricolaVolontario)) {
            System.out.println("2. Rimuovi disponibilità");
        }
        // Controlla se ci sono notifiche non lette e mostra il punto 4 solo se necessario
        if (notificaDAO.ciSonoNotificheNonLette(matricolaVolontario)) {
            System.out.println("3. Visualizza notifiche");
        }

        if (utenteDAO.haServiziOEmergenzeAssegnate(matricolaVolontario)) {
            System.out.println("4. Visualizza servizi assegnati");
        }

        System.out.println("5. Esci");
        System.out.println(" ");
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                VolontarioController.inserisciDisponibilita(scanner, matricolaVolontario);
                break;
            case 2:
                DisponibilitaDAO.rimuoviDisponibilita(scanner, matricolaVolontario);
                break;
            case 3:
                // Visualizzare le notifiche e segnarle come lette
                notificaDAO.visualizzaNotifiche(scanner, matricolaVolontario);
                break;
            case 4:
                utenteDAO.visualizzaServiziEEmergenzeAssegnate(scanner, matricolaVolontario);
                break;
            case 5:
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
        System.out.println("4. Gestisci pazienti");


        // Verifica se ci sono richieste di rimozione
        if (servizioDAO.ciSonoRichiesteRimozione()) {
            System.out.println("6. Visualizza RICHIESTE URGENTI rimozione disponibilità");
        }

        // Verifica se ci sono disponibilità non confermate o notifiche non lette
        if (utenteDAO.ciSonoDisponibilitaENotificheNonLette()) {
            System.out.println("7. Visualizza disponibilità e notifiche non lette");
        }

        System.out.println("8. Esci");
        System.out.println(" ");
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                mostraMenuMezzi(scanner);
                break;
            case 2:
                mostraMenuVolontari(scanner);
                break;
            case 3:
                mostraMenuServizi(scanner);
                break;
            case 4:
                mostraMenuPazienti(scanner);
                break;
            case 6:
                if (servizioDAO.ciSonoRichiesteRimozione()) {
                    AmministratoreController.visualizzaRichiesteRimozione(scanner);
                } else {
                    System.out.print(" ");
                }
                break;
            case 7:
                // Visualizza disponibilità e notifiche non lette
                utenteDAO.visualizzaDisponibilitaENotificheNonLette(scanner);
                break;
            case 8:
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
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                AmministratoreController.aggiungiMezzo(scanner);
                break;
            case 2:
                mezzoDAO.modificaMezzo(scanner);
                break;
            case 3:
                mezzoDAO.eliminaMezzo(scanner);
                break;
            case 4:
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
        System.out.println("3. Mostra tutti i volontari");
        System.out.println("4. Torna al menu amministratore");
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                utenteDAO.modificaAnagrafeVolontariDAO(scanner);
                break;
            case 2:
                utenteDAO.eliminaVolontario(scanner);
                break;
            case 3:
                utenteDAO.mostraListaVolontari(scanner);
                break;
            case 4:
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
        System.out.println("2. Modifica servizio");
        System.out.println("3. Elimina dati servizio");
        System.out.println("4. Assegna automaticamente i volontari");
        System.out.println("5. Torna al menu amministratore");
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                servizioDAO.aggiungiServizioDAO(scanner);
                break;
            case 2:
                servizioDAO.modificaServizio(scanner);
                break;
            case 3:
                servizioDAO.eliminaServizio(scanner);
                break;
            case 4:
                AmministratoreController.assegnaAutomaticamente(scanner);
                break;
            case 5:
                mostraMenuAdmin(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }
    //MENU PAZIENTI
    public static void mostraMenuPazienti(Scanner scanner) {
        System.out.println("Menu gestione pazienti:");
        System.out.println("1. Visualizza tutti i pazienti");
        System.out.println("2. Aggiungi paziente");
        System.out.println("3. Modifica paziente");
        System.out.println("4. Elimina paziente");
        System.out.println("5. Torna al menu amministratore");
        System.out.print("Seleziona un'opzione: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                pazienteDAO.visualizzaPazienti(scanner);
                break;
            case 2:
                boolean menu=false;
                AmministratoreController.aggiungiPaziente(scanner);
                break;
            case 3:
                pazienteDAO.modificaPaziente(scanner);
                break;
            case 4:
                pazienteDAO.eliminaPaziente(scanner);
                break;
            case 5:
                mostraMenuAdmin(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }

}

