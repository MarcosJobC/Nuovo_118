import java.sql.Time;
import java.util.List;

public class Disponibilita {
    private int id;
    private int matricolaVolontario;
    private String dataDisponibilita;
    private String tipologia;
    private String stato; // Confermata o Non confermata


    public Disponibilita(int id, int matricolaVolontario, String dataDisponibilita, String tipologia, String stato) {
        this.id = id;
        this.matricolaVolontario = matricolaVolontario;
        this.dataDisponibilita = dataDisponibilita;
        this.tipologia = tipologia;
        this.stato = stato;
    }

    // Metodi getter
    public int getId() {
        return id;
    }

    public int getMatricolaVolontario() {
        return matricolaVolontario;
    }

    public String getDataDisponibilita() {
        return dataDisponibilita;
    }

    public String getTipologia() {
        return tipologia;
    }

    public String getStato() {
        return stato;
    }

    // Metodi setter
    public void setId(int id) {
        this.id = id;
    }

    public void setMatricolaVolontario(int matricolaVolontario) {
        this.matricolaVolontario = matricolaVolontario;
    }

    public void setDataDisponibilita(String dataDisponibilita) {
        this.dataDisponibilita = dataDisponibilita;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}