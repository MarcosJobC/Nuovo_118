package DomainModel;

public class Mezzo {
    private String siglaMezzo;
    private String targa;
    private String tipologia;

    // Costruttore
    public Mezzo(String siglaMezzo, String targa, String tipologia) {
        this.siglaMezzo = siglaMezzo;
        this.targa = targa;
        this.tipologia = tipologia;
    }

    // Getter per siglaMezzo
    public String getSiglaMezzo() {
        return siglaMezzo;
    }

    // Setter per siglaMezzo
    public void setSiglaMezzo(String siglaMezzo) {
        this.siglaMezzo = siglaMezzo;
    }

    // Getter per targa
    public String getTarga() {
        return targa;
    }

    // Setter per targa
    public void setTarga(String targa) {
        this.targa = targa;
    }

    // Getter per tipologia
    public String getTipologia() {
        return tipologia;
    }

    // Setter per tipologia
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
}
