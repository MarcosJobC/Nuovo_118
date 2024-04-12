package Main.DomainModel;

import java.sql.Time;

public class Servizio {
    private int id;
    private String data;
    private Time orario;
    private Mezzo mezzo;
    private Utente autista;
    private Utente soccorritore;
    private Paziente paziente;
    public Servizio(int id, String data, Time orario, Mezzo mezzo, Utente autista, Utente soccorritore, Paziente paziente) {
        this.id = id;
        this.data = data;
        this.orario = orario;
        this.mezzo = mezzo;
        this.autista = autista;
        this.soccorritore = soccorritore;
        this.paziente = paziente;
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
    public Mezzo getMezzo() {
        return mezzo;
    }

    // Setter per siglamezzo
    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
    }

    // Getter per autista
    public Utente getAutista() {
        return autista;
    }

    // Setter per autista
    public void setAutista(Utente autista) {
        this.autista = autista;
    }

    // Getter per soccorritore
    public Utente getSoccorritore() {
        return soccorritore;
    }

    // Setter per soccorritore
    public void setSoccorritore(Utente soccorritore) {
        this.soccorritore = soccorritore;
    }


    // Getter per paziente
    public Paziente getPaziente() {
        return paziente;
    }

    // Setter per paziente
    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }
}
