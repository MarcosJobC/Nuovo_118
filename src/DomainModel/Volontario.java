package DomainModel;

import ORM.volontariManager;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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


}