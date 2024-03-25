package BusinessLogic;

import DomainModel.Utente;
import ORM.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class menuController {

    //MENU INIZIALE
    public static void menuIniziale(Scanner scanner) {

        boolean sceltaValida = false;

        System.out.println("Benvenuto al Gestionale Turni!");
        while (!sceltaValida) {
            System.out.print("Vuoi fare l'accesso o registrarti o uscire? (A/R/U): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("R")) {
                sceltaValida = true;
                registrazione(scanner, sceltaValida);
            } else if (choice.equalsIgnoreCase("A")) {
                sceltaValida = true;
                accesso(scanner, sceltaValida);
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
        if (notificaDAO.ciSonoNotificheNonLetteDAO(matricolaVolontario)) {
            System.out.println("3. Visualizza notifiche");
        }

        if (utenteDAO.haServiziAssegnati(matricolaVolontario)) {
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
                DisponibilitaDAO.rimuoviDisponibilitaDAO(scanner, matricolaVolontario);
                break;
            case 3:
                // Visualizzare le notifiche e segnarle come lette
                VolontarioController.visualizzaNotifiche(scanner, matricolaVolontario);
                break;
            case 4:
                VolontarioController.visualizzaServiziAssegnati(scanner, matricolaVolontario);
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
        if (servizioDAO.ciSonoRichiesteRimozioneDAO()) {
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
                if (servizioDAO.ciSonoRichiesteRimozioneDAO()) {
                    AmministratoreController.visualizzaRichiesteRimozione(scanner);
                } else {
                    System.out.print(" ");
                }
                break;
            case 7:
                // Visualizza disponibilità e notifiche non lette
                AmministratoreController.visualizzaDisponibilitaENotificheNonLette(scanner);
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
                AmministratoreController.modificaMezzo(scanner);
                break;
            case 3:
                AmministratoreController.eliminaMezzo(scanner);
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
                AmministratoreController.modificaUtente(scanner);
                break;
            case 2:
                AmministratoreController.eliminaUtente(scanner);
                break;
            case 3:
                AmministratoreController.mostraListaVolontari(scanner);
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
                AmministratoreController.aggiungiServizio(scanner);
                break;
            case 2:
                AmministratoreController.modificaServizio(scanner);
                break;
            case 3:
                AmministratoreController.eliminaServizio(scanner);
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
                AmministratoreController.visualizzaPazienti(scanner);
                break;
            case 2:
                AmministratoreController.aggiungiPaziente(scanner);
                break;
            case 3:
                AmministratoreController.modificaPaziente(scanner);
                break;
            case 4:
                AmministratoreController.eliminaPaziente(scanner);
                break;
            case 5:
                mostraMenuAdmin(scanner);
                break;
            default:
                System.out.println("Scelta non valida.");
        }
    }

    public static void registrazione(Scanner scanner, boolean sceltaValida) {
        sceltaValida = true;
        System.out.print("Inserisci il nome: ");
        String nome = scanner.nextLine();

        while (nome.isEmpty()) {
            System.out.print("Il nome non può essere vuoto. Inserisci il nome: ");
            nome = scanner.nextLine();
        }

        System.out.print("Inserisci il cognome: ");
        String cognome = scanner.nextLine();

        while (cognome.isEmpty()) {
            System.out.print("Il cognome non può essere vuoto. Inserisci il cognome: ");
            cognome = scanner.nextLine();
        }

        String dataDiNascita;
        LocalDate dataNascita = null;

        while (true) {
            System.out.print("Inserisci la data di nascita (dd-MM-yyyy): ");
            dataDiNascita = scanner.nextLine();

            if (dataDiNascita.isEmpty()) {
                System.out.println("La data di nascita non può essere vuota. Riprova.");
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                dataNascita = LocalDate.parse(dataDiNascita, formatter);

                // Calcola la differenza in anni tra la data di nascita e la data attuale
                int anniDifferenza = Period.between(dataNascita, LocalDate.now()).getYears();

                if (anniDifferenza < 16) {
                    System.out.println("Devi avere almeno 16 anni per registrarti.");
                    continue;
                }

                break;  // Esci dal ciclo se la data di nascita è valida

            } catch (DateTimeParseException e) {
                System.out.println("Formato data non valido. Utilizza il formato dd-MM-yyyy.");
            }
        }

        int sceltaQualifica = 0;

        while (sceltaQualifica < 1 || sceltaQualifica > 3) {
            System.out.print("Scegli la qualifica tra: 1 AUTISTA | 2 SOCCORITORE | 3 CENTRALINISTA: ");

            try {
                sceltaQualifica = scanner.nextInt();
                scanner.nextLine();  // Consuma il newline

                if (sceltaQualifica < 1 || sceltaQualifica > 3) {
                    System.out.println("Scelta non valida. Inserisci un numero tra 1|2|3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserisci un numero valido.");
                scanner.nextLine();  // Consuma l'input non valido
            }
        }

        String qualifica = "";

        switch (sceltaQualifica) {
            case 1:
                qualifica = "Autista";
                break;
            case 2:
                qualifica = "Soccorritore";
                break;
            case 3:
                qualifica = "Centralinista";
                break;
        }

        System.out.print("Inserisci il codice fiscale: ");
        String codicefiscale = scanner.nextLine();

        while (codicefiscale.isEmpty()) {
            System.out.print("Il codice fiscale non può essere vuoto. Inserisci il codice fiscale: ");
            codicefiscale = scanner.nextLine();
        }

        System.out.print("Inserisci la password: ");
        String password = scanner.nextLine();

        while (password.isEmpty()) {
            System.out.print("La password non può essere vuota. Inserisci la password: ");
            password = scanner.nextLine();
        }

        // Creazione dell'oggetto Utente
        Utente utente = new Utente(0, nome, cognome, dataDiNascita, qualifica, codicefiscale, password, false);

        // Passaggio dell'oggetto Utente al metodo registrazioneDAO
        utenteDAO.registrazioneDAO(utente);
        menuController.menuIniziale(scanner);
    }

    public static void accesso(Scanner scanner, boolean sceltaValida) {
        sceltaValida = true;
        System.out.print("Inserisci il codice fiscale: ");
        String codicefiscale = scanner.nextLine();

        while (codicefiscale.isEmpty()) {
            System.out.print("Il codice fiscale non può essere vuoto. Inserisci il codice fiscale o scrivi exit per uscire: ");
            codicefiscale = scanner.nextLine();
            if (codicefiscale.equalsIgnoreCase("exit")) {
                sceltaValida = false;
                System.out.println(" ");
                System.out.println(" ");
                menuController.menuIniziale(scanner);
                return; // Esci dal metodo per evitare ulteriori operazioni
            }
        }

        System.out.print("Inserisci la password: ");
        String password = scanner.nextLine();

        while (password.isEmpty()) {
            System.out.print("La password non può essere vuota. Inserisci la password: ");
            password = scanner.nextLine();
        }

        utenteDAO.accessoDAO(codicefiscale,password,scanner);

    }

}

