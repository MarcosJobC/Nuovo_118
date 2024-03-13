package DomainModel;

import java.sql.Time;
import java.util.List;


public class Servizio {
    private int id;
    private String data;
    private Time orario;
    private List<Integer> volontariAssegnati;
    private String siglaMezzo;

    public Servizio(int id, String data, Time orario, List<Integer> volontariAssegnati, String siglaMezzo) {
        this.id = id;
        this.data = data;
        this.orario = orario;
        this.volontariAssegnati = volontariAssegnati;
        this.siglaMezzo = siglaMezzo;
    }

    // Metodi getter
    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public Time getOrario() {
        return orario;
    }

    public List<Integer> getVolontariAssegnati() {
        return volontariAssegnati;
    }

    public String getSiglaMezzo() {
        return siglaMezzo;
    }

    // Metodi setter
    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOrario(Time orario) {
        this.orario = orario;
    }

    public void setVolontariAssegnati(List<Integer> volontariAssegnati) {
        this.volontariAssegnati = volontariAssegnati;
    }

    public void setSiglaMezzo(String siglaMezzo) {
        this.siglaMezzo = siglaMezzo;
    }
}




