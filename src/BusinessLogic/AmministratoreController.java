package BusinessLogic;
import ORM.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;



public class AmministratoreController {

    //TODO fare che visualizza pazienti esegue queste cose:
    /*
    1- funzione qui chiama funzione in dao
    2- funzione in dao salva risultati in arraylist e ritorna arraylist a funzione qui
    3- funzione qui stampa risultati
     */

    public static void aggiungiMezzo(Scanner scanner) {
        String siglaMezzo;
        String targa;
        String tipologia;

        do {
            System.out.println("Inserisci la sigla del mezzo:");
            siglaMezzo = scanner.nextLine().trim(); // Rimuovi spazi iniziali e finali

            if (siglaMezzo.isEmpty()) {
                System.out.println("La sigla del mezzo non può essere vuota. Riprova.");
            }
        } while (siglaMezzo.isEmpty());

        do {
            System.out.println("Inserisci la targa del mezzo:");
            targa = scanner.nextLine().trim(); // Rimuovi spazi iniziali e finali

            if (targa.isEmpty()) {
                System.out.println("La targa del mezzo non può essere vuota. Riprova.");
            }
        } while (targa.isEmpty());

        do {
            System.out.println("Seleziona la tipologia del mezzo:");
            System.out.println("1. Auto");
            System.out.println("2. Ambulanza");
            System.out.println("3. Mezzo attrezzato");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = 0; // Impostiamo una scelta non valida in caso di input non numerico
            }

            switch (choice) {
                case 1:
                    tipologia = "Auto";
                    break;
                case 2:
                    tipologia = "Ambulanza";
                    break;
                case 3:
                    tipologia = "BusinessLogic.Mezzo attrezzato";
                    break;
                default:
                    System.out.println("Scelta non valida. Devi selezionare 1, 2 o 3.");
                    tipologia = ""; // Imposta tipologia a vuoto per ripetere il ciclo
            }
        } while (tipologia.isEmpty());
        mezzoDAO.aggiungiMezzoDAO(siglaMezzo,  targa, tipologia );
        menuController.mostraMenuMezzi(scanner);
    }

    public static void aggiungiPaziente(Scanner scanner){
        System.out.println(" ");
        System.out.println("INSERIMENTO NUOVO PAZIENTE:");

        String nomePaziente, cognomePaziente, dataNascitaString, luogoNascita, indirizzoResidenza;
        LocalDate dataNascita = null;

        // Richiedi e valida il nome del paziente
        do {
            System.out.print("Inserisci il nome del paziente: ");
            nomePaziente = scanner.nextLine();
            if (nomePaziente.isEmpty()) {
                System.out.println("Il campo nome non può essere lasciato vuoto.");
            }
        } while (nomePaziente.isEmpty());

        // Richiedi e valida il cognome del paziente
        do {
            System.out.print("Inserisci il cognome del paziente: ");
            cognomePaziente = scanner.nextLine();
            if (cognomePaziente.isEmpty()) {
                System.out.println("Il campo cognome non può essere lasciato vuoto.");
            }
        } while (cognomePaziente.isEmpty());

        // Richiedi e valida la data di nascita del paziente
        do {
            System.out.print("Inserisci la data di nascita del paziente (formato dd-MM-yyyy): ");
            dataNascitaString = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                dataNascita = LocalDate.parse(dataNascitaString, formatter);
                if (dataNascita.isAfter(LocalDate.now())) {
                    System.out.println("La data di nascita non può essere successiva a oggi.");
                    dataNascita = null;
                }
            } catch (DateTimeParseException e) {
                dataNascita = null;
            }
            if (dataNascita == null) {
                System.out.println("Inserisci una data di nascita valida.");
            }
        } while (dataNascita == null);

        // Richiedi e valida il luogo di nascita del paziente
        do {
            System.out.print("Inserisci il luogo di nascita del paziente: ");
            luogoNascita = scanner.nextLine();
            if (luogoNascita.isEmpty()) {
                System.out.println("Il campo luogo di nascita non può essere lasciato vuoto.");
            }
        } while (luogoNascita.isEmpty());

        // Richiedi e valida l'indirizzo di residenza del paziente
        do {
            System.out.print("Inserisci l'indirizzo di residenza del paziente: ");
            indirizzoResidenza = scanner.nextLine();
            if (indirizzoResidenza.isEmpty()) {
                System.out.println("Il campo indirizzo di residenza non può essere lasciato vuoto.");
            }
        } while (indirizzoResidenza.isEmpty());

        pazienteDAO.aggiungiPazienteDAO(scanner,nomePaziente, cognomePaziente, dataNascita, luogoNascita, indirizzoResidenza);

        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuPazienti(scanner);
    }
    public static void aggiungiPazientedaServizio(Scanner scanner,boolean newfromservizio,String dataServizio,LocalTime orarioServizio){
        System.out.println(" ");
        System.out.println("INSERIMENTO NUOVO PAZIENTE:");

        String nomePaziente, cognomePaziente, dataNascitaString, luogoNascita, indirizzoResidenza;
        LocalDate dataNascita = null;

        // Richiedi e valida il nome del paziente
        do {
            System.out.print("Inserisci il nome del paziente: ");
            nomePaziente = scanner.nextLine();
            if (nomePaziente.isEmpty()) {
                System.out.println("Il campo nome non può essere lasciato vuoto.");
            }
        } while (nomePaziente.isEmpty());

        // Richiedi e valida il cognome del paziente
        do {
            System.out.print("Inserisci il cognome del paziente: ");
            cognomePaziente = scanner.nextLine();
            if (cognomePaziente.isEmpty()) {
                System.out.println("Il campo cognome non può essere lasciato vuoto.");
            }
        } while (cognomePaziente.isEmpty());

        // Richiedi e valida la data di nascita del paziente
        do {
            System.out.print("Inserisci la data di nascita del paziente (formato dd-MM-yyyy): ");
            dataNascitaString = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                dataNascita = LocalDate.parse(dataNascitaString, formatter);
                if (dataNascita.isAfter(LocalDate.now())) {
                    System.out.println("La data di nascita non può essere successiva a oggi.");
                    dataNascita = null;
                }
            } catch (DateTimeParseException e) {
                dataNascita = null;
            }
            if (dataNascita == null) {
                System.out.println("Inserisci una data di nascita valida.");
            }
        } while (dataNascita == null);

        // Richiedi e valida il luogo di nascita del paziente
        do {
            System.out.print("Inserisci il luogo di nascita del paziente: ");
            luogoNascita = scanner.nextLine();
            if (luogoNascita.isEmpty()) {
                System.out.println("Il campo luogo di nascita non può essere lasciato vuoto.");
            }
        } while (luogoNascita.isEmpty());

        // Richiedi e valida l'indirizzo di residenza del paziente
        do {
            System.out.print("Inserisci l'indirizzo di residenza del paziente: ");
            indirizzoResidenza = scanner.nextLine();
            if (indirizzoResidenza.isEmpty()) {
                System.out.println("Il campo indirizzo di residenza non può essere lasciato vuoto.");
            }
        } while (indirizzoResidenza.isEmpty());

        pazienteDAO.aggiungiPazientedaServizioDAO(scanner,nomePaziente, cognomePaziente, dataNascitaString, luogoNascita, indirizzoResidenza, dataNascita, dataServizio, orarioServizio);

        System.out.println(" ");
        servizioDAO.aggiungiServizioInternoDAO(scanner, newfromservizio, dataServizio, orarioServizio);
    }
    public static void visualizzaRichiesteRimozione(Scanner scanner) {
        servizioDAO.visualizzaRichiesteRimozioneDAO(scanner);
    }
    public static void assegnaAutomaticamente(Scanner scanner) {
        //Vengono assegnati prima tutti gli autisti e successivamente tutti i soccoritori
        servizioDAO.assegnaAutistiAutomaticamente();
        servizioDAO.assegnaSoccorritoriAutomaticamente();
        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuServizi(scanner);
    }

}