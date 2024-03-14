package DomainModel;

import java.sql.Time;
import java.util.List;


public class Servizio {
    private int id;
    private String data;
    private Time orario;
    private List<Integer> volontariAssegnati;
    private Mezzo mezzo;

    public Servizio(int id, String data, Time orario, List<Integer> volontariAssegnati, Mezzo mezzo) {
        this.id = id;
        this.data = data;
        this.orario = orario;
        this.volontariAssegnati = volontariAssegnati;
        this.mezzo = mezzo;
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




