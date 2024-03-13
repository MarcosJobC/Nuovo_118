package DomainModel;

public class Mezzo {
    private String siglaMezzo;
    private String targa;
    private String tipologia;

    public Mezzo(String siglaMezzo, String targa, String tipologia) {
        this.siglaMezzo = siglaMezzo;
        this.targa = targa;
        this.tipologia = tipologia;
    }


    // Metodi getter
    public String getSiglaMezzo() {
        return siglaMezzo;
    }

    public String getTarga() {
        return targa;
    }

    public String getTipologia() {
        return tipologia;
    }

    // Metodi setter
    public void setSiglaMezzo(String siglaMezzo) {
        this.siglaMezzo = siglaMezzo;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
}