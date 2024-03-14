package BusinessLogic;

import ORM.pazienteDAO;
import ORM.servizioDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class pazientiController {

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

}
