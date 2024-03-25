package DomainModel;


import java.time.LocalDate;

public class Paziente {

    private int id;
    private String nomePaziente;
    private String cognomePaziente;
    private LocalDate dataNascita; // Modifica il tipo di dato a LocalDate
    private String luogoNascita;
    private String indirizzoResidenza;

    public Paziente(int id, String nomePaziente, String cognomePaziente, LocalDate dataNascita, String luogoNascita, String indirizzoResidenza) {
        this.id = id;
        this.nomePaziente = nomePaziente;
        this.cognomePaziente = cognomePaziente;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.indirizzoResidenza = indirizzoResidenza;
    }

    // Getter per id
    public int getId() {
        return id;
    }

    // Setter per id
    public void setId(int id) {
        this.id = id;
    }

    // Getter per nomePaziente
    public String getNomePaziente() {
        return nomePaziente;
    }

    // Setter per nomePaziente
    public void setNomePaziente(String nomePaziente) {
        this.nomePaziente = nomePaziente;
    }

    // Getter per cognomePaziente
    public String getCognomePaziente() {
        return cognomePaziente;
    }

    // Setter per cognomePaziente
    public void setCognomePaziente(String cognomePaziente) {
        this.cognomePaziente = cognomePaziente;
    }

    // Getter per dataNascita
    public LocalDate getDataNascita() {
        return dataNascita;
    }

    // Setter per dataNascita
    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    // Getter per luogoNascita
    public String getLuogoNascita() {
        return luogoNascita;
    }

    // Setter per luogoNascita
    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    // Getter per indirizzoResidenza
    public String getIndirizzoResidenza() {
        return indirizzoResidenza;
    }

    // Setter per indirizzoResidenza
    public void setIndirizzoResidenza(String indirizzoResidenza) {
        this.indirizzoResidenza = indirizzoResidenza;
    }
}
