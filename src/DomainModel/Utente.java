package DomainModel;


public class Utente {

    private int matricola;
    private String nome;
    private String cognome;
    private String datadinascita; // Nota: mancava un punto e virgola qui
    private String qualifica; // Nota: c'era un punto e virgola extra qui
    private String codiceFiscale;
    private String password;
    private Boolean isAdmin; // Nota: Potrebbe essere meglio utilizzare un tipo booleano per isAdmin

    // Costruttore
    public Utente(int matricola, String nome, String cognome, String datadinascita, String qualifica, String codiceFiscale, String password, Boolean isAdmin) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.datadinascita = datadinascita;
        this.qualifica = qualifica;
        this.codiceFiscale = codiceFiscale;
        this.password = password;
        this.isAdmin = false;
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

    public String getDatadinascita() {
        return datadinascita;
    }

    public String getQualifica() {
        return qualifica;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
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

    public void setDatadinascita(String datadinascita) {
        this.datadinascita = datadinascita;
    }

    public void setQualifica(String qualifica) {
        this.qualifica = qualifica;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}