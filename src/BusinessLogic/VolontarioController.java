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


public class VolontarioController {

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
        utenteDAO.registrazioneDAO(nome,cognome,dataDiNascita,qualifica,codicefiscale,password);

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
    public static void inserisciDisponibilita(Scanner scanner, int matricolaVolontario) {
        String dataDisponibilita = "";
        LocalDateTime dataOdierna = LocalDateTime.now();
        LocalTime oraInizio = null;
        LocalTime oraFine = null;

        while (dataDisponibilita.isEmpty()) {
            System.out.print("Inserisci la data della disponibilità (dd-MM-yyyy): ");
            dataDisponibilita = scanner.nextLine();

            if (dataDisponibilita.isEmpty()) {
                System.out.println("La data non può essere vuota. Riprova.");
            } else {
                try {
                    LocalDateTime dataInserita = LocalDateTime.of(LocalDate.parse(dataDisponibilita, DateTimeFormatter.ofPattern("dd-MM-yyyy")), LocalTime.MIDNIGHT);

                    if (dataInserita.isBefore(dataOdierna.minusDays(1))) {
                        System.out.println("La data della disponibilità non può essere antecedente a oggi.");
                        dataDisponibilita = "";
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato data non valido. Utilizza il formato dd-MM-yyyy.");
                    dataDisponibilita = "";
                }
            }
        }

        System.out.print("Seleziona la tipologia del servizio: Servizi sociali (S) | lascia vuoto per qualsiasi ruolo: ");
        String sceltaTipologia = scanner.nextLine().toUpperCase();

        String tipologia;
        switch (sceltaTipologia) {
            case "S":
                tipologia = "Servizi sociali";
                break;
            default:
                System.out.println("Tipologia impostata a: qualsiasi ruolo.");
                tipologia = "Servizi sociali";
        }


        // Condizione per richiedere l'orario di inizio e fine solo per "Servizi sociali"
        if ("Servizi sociali".equals(tipologia)) {
            while (oraInizio == null) {
                try {
                    System.out.print("Inserisci l'orario di inizio (HH:mm): ");
                    String inputOraInizio = scanner.nextLine();
                    oraInizio = LocalTime.parse(inputOraInizio, DateTimeFormatter.ofPattern("HH:mm"));
                } catch (DateTimeParseException e) {
                    System.out.println("Formato orario non valido. Utilizza il formato HH:mm.");
                }
            }

            while (oraFine == null || oraFine.isBefore(oraInizio)) {
                try {
                    System.out.print("Inserisci l'orario di fine (HH:mm): ");
                    String inputOraFine = scanner.nextLine();
                    oraFine = LocalTime.parse(inputOraFine, DateTimeFormatter.ofPattern("HH:mm"));

                    if (oraFine.isBefore(oraInizio)) {
                        System.out.println("L'orario di fine deve essere successivo all'orario di inizio.");
                        oraFine = null;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato orario non valido. Utilizza il formato HH:mm.");
                }
            }
        }
        DisponibilitaDAO.inserisciDisponibilitaDAO(scanner,matricolaVolontario,dataDisponibilita,tipologia,oraFine,oraInizio);
    }


}