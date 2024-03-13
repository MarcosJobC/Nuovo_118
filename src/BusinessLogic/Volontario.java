package BusinessLogic;
import ORM.*;

import java.sql.Time;
import java.util.List;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;



public class Volontario {


    private static Connection connection;
    public Volontario(Connection connection) {
        this.connection = connection;
    }



    private int matricola;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String qualifica;
    // Altri campi e metodi

    public Volontario(int matricola, String nome, String cognome, String codiceFiscale, String qualifica) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.qualifica = qualifica;
    }

    // Metodi getter
    public int getMatricola() {
        return matricola;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getQualifica() {
        return qualifica;
    }

    // Metodi setter
    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public void setQualifica(String qualifica) {
        this.qualifica = qualifica;
    }



    //TODO NUOVO METODO SMEZZATO PER REGISTRAZIONE
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
        volontariManager.registrazioneDAO(nome,cognome,dataDiNascita,qualifica,codicefiscale,password);

    }

}