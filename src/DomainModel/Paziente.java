package DomainModel;


public class Paziente {

    private String nomePaziente;
    private String cognomePaziente;
    private String dataNascita;
    private String luogoNascita;
    private String indirizzoResidenza;

    // Altri campi e metodi

    public Paziente(String nomePaziente, String cognomePaziente, String dataNascita, String luogoNascita, String indirizzoResidenza) {
        this.nomePaziente = nomePaziente;
        this.cognomePaziente = cognomePaziente;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.indirizzoResidenza = indirizzoResidenza;
    }

    // Metodi getter
    public String getNomePaziente() {
        return nomePaziente;
    }

    public String getCognomePaziente() {
        return cognomePaziente;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    // Metodi setter
    public void setIndirizzoResidenza(int indirizzoResidenza) {
        this.indirizzoResidenza = String.valueOf(indirizzoResidenza);
    }


}
