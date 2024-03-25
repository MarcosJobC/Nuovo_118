package BusinessLogic;
import DomainModel.Mezzo;
import DomainModel.Paziente;
import ORM.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;



public class AmministratoreController {

    //TODO fare che visualizza pazienti esegue queste cose:
    // 1- funzione qui chiama funzione in dao
    // 2- funzione in dao salva risultati in arraylist e ritorna arraylist qui
    // 3- qui vengono stampati i risultati contenuti nell' arraylist

    //Mezzi
    public static Mezzo aggiungiMezzo(Scanner scanner) {
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
                    tipologia = "Mezzo attrezzato";
                    break;
                default:
                    System.out.println("Scelta non valida. Devi selezionare 1, 2 o 3.");
                    tipologia = ""; // Imposta tipologia a vuoto per ripetere il ciclo
            }
        } while (tipologia.isEmpty());

        Mezzo mezzo = new Mezzo(siglaMezzo, targa, tipologia);
        mezzoDAO.aggiungiMezzoDAO(mezzo);
        menuController.mostraMenuMezzi(scanner);
        return mezzo; // Restituisce il mezzo appena creato
    }

    public static void modificaMezzo(Scanner scanner) {
        mezzoDAO.modificaMezzoDAO(scanner);
    }
    public static void eliminaMezzo(Scanner scanner) {
        mezzoDAO.eliminaMezzoDAO(scanner);
    }

    //Pazienti
    public static Paziente aggiungiPaziente(Scanner scanner){
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

        Paziente paziente = new Paziente(0, nomePaziente, cognomePaziente, dataNascita, luogoNascita, indirizzoResidenza);
        pazienteDAO.aggiungiPazienteDAO(paziente);
        menuController.mostraMenuPazienti(scanner);
        return paziente;
    }
    public static void aggiungiPazientedaServizio(Scanner scanner, boolean newfromservizio, String dataServizio, LocalTime orarioServizio) {
        System.out.println(" ");
        System.out.println("INSERIMENTO NUOVO PAZIENTE:");

        String nomePaziente, cognomePaziente, luogoNascita, indirizzoResidenza;
        LocalDate dataNascita = null; // Dichiarazione iniziale di dataNascita

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
        String dataNascitaString; // Dichiarazione della variabile dataNascitaString
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

        // Crea un nuovo paziente con l'oggetto LocalDate per la data di nascita
        Paziente nuovoPaziente = new Paziente(0, nomePaziente, cognomePaziente, dataNascita, luogoNascita, indirizzoResidenza);
        pazienteDAO.aggiungiPazientedaServizioDAO(nuovoPaziente);

        System.out.println(" ");
        servizioDAO.aggiungiServizioInternoDAO(scanner, newfromservizio, dataServizio, orarioServizio);
    }




    public static void modificaPaziente(Scanner scanner) {
        pazienteDAO.modificaPazienteDAO(scanner);
    }
    public static void eliminaPaziente(Scanner scanner) {
        pazienteDAO.eliminaPazienteDAO(scanner);
    }
    public static void visualizzaPazienti(Scanner scanner) {
        pazienteDAO.visualizzaPazientiDAO(scanner);
    }

    //Servizi
    public static void aggiungiServizio(Scanner scanner) {
        servizioDAO.aggiungiServizioDAO(scanner);
    }
    public static void modificaServizio(Scanner scanner) {
        servizioDAO.modificaServizioDAO(scanner);
    }
    public static void eliminaServizio(Scanner scanner) {
        servizioDAO.eliminaServizioDAO(scanner);
    }
    public static void assegnaAutomaticamente(Scanner scanner) {
        //Vengono assegnati prima tutti gli autisti e successivamente tutti i soccoritori
        servizioDAO.assegnaAutistiAutomaticamenteDAO();
        servizioDAO.assegnaSoccorritoriAutomaticamenteDAO();
        System.out.println(" ");
        System.out.println(" ");
        menuController.mostraMenuServizi(scanner);
    }

    //Disponibilità
    public static void visualizzaRichiesteRimozione(Scanner scanner) {
        DisponibilitaDAO.visualizzaRichiesteRimozioneDAO(scanner);
    }
    public static void accettaRichiesteRimozione(int idRichiesta, Scanner scanner) {
        DisponibilitaDAO.accettaRichiestaRimozioneDAO(idRichiesta,scanner);
    }
    public static void cancellaRichiesteRimozione(int idRichiesta) {
        DisponibilitaDAO.cancellaRichiestaRimozioneDAO(idRichiesta);
    }

    //Utenti
    public static void modificaUtente(Scanner scanner) {
        utenteDAO.modificaUtenteDAO(scanner);
    }
    public static void eliminaUtente(Scanner scanner) {
        utenteDAO.eliminaUtenteDAO(scanner);
    }
    public static void visualizzaDisponibilitaENotificheNonLette(Scanner scanner) {
        utenteDAO.visualizzaDisponibilitaENotificheNonLetteDAO(scanner);
    }
    public static void mostraListaVolontari(Scanner scanner) {
        utenteDAO.mostraListaVolontariDAO(scanner);
    }


}