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
    public static void visualizzaNotifiche(Scanner scanner, int matricolaVolontario) {
        notificaDAO.visualizzaNotificheDAO(scanner, matricolaVolontario);
    }
    public static void visualizzaServiziAssegnati(Scanner scanner, int matricolaVolontario) {
        servizioDAO.visualizzaServiziAssegnatiDAO(scanner, matricolaVolontario);
    }
}