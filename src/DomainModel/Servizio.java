package DomainModel;

import java.sql.Time;
import java.util.List;


import java.sql.Time; // Assicurati di importare Time da java.sql o utilizza la libreria appropriata

public class Servizio {
    private int id;
    private String data;
    private Time orario;
    private String siglamezzo;
    private int Autista;
    private int Soccorritore;

    public Servizio(int id, String data, Time orario, String siglamezzo, int Autista, int Soccorritore) {
        this.id = id;
        this.data = data;
        this.orario = orario;
        this.siglamezzo = siglamezzo;
        this.Autista = Autista;
        this.Soccorritore = Soccorritore;
    }

    // Getter per id
    public int getId() {
        return id;
    }

    // Setter per id
    public void setId(int id) {
        this.id = id;
    }

    // Getter per data
    public String getData() {
        return data;
    }

    // Setter per data
    public void setData(String data) {
        this.data = data;
    }

    // Getter per orario
    public Time getOrario() {
        return orario;
    }

    // Setter per orario
    public void setOrario(Time orario) {
        this.orario = orario;
    }

    // Getter per siglamezzo
    public String getSiglamezzo() {
        return siglamezzo;
    }

    // Setter per siglamezzo
    public void setSiglamezzo(String siglamezzo) {
        this.siglamezzo = siglamezzo;
    }

    // Getter per Autista
    public int getAutista() {
        return Autista;
    }

    // Setter per Autista
    public void setAutista(int Autista) {
        this.Autista = Autista;
    }

    // Getter per Soccorritore
    public int getSoccorritore() {
        return Soccorritore;
    }

    // Setter per Soccorritore
    public void setSoccorritore(int Soccorritore) {
        this.Soccorritore = Soccorritore;
    }
}





