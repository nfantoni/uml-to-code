package dao;

import java.util.Date;

public class SpettacoloDTO {

    private String codiceSpettacolo;
    private String nomeArtista;
    private Date data;
    private String genere;
    private String nomeTeatro;

    public SpettacoloDTO() {

    }

    public String getCodiceSpettacolo() { return codiceSpettacolo; }

    public void  setCodiceSpettacolo(String codiceSpettacolo) { this.codiceSpettacolo = codiceSpettacolo; }

    public String getNomeArtista() { return nomeArtista; }

    public void  setNomeArtista(String nomeArtista) { this.nomeArtista = nomeArtista; }

    public Date getData() { return data; }

    public void  setData(Date data) { this.data = data; }

    public String getGenere() { return genere; }

    public void  setGenere(String genere) { this.genere = genere; }

    public String getNomeTeatro() { return nomeTeatro; }

    public void setNomeTeatro() { this.nomeTeatro = nomeTeatro; }

}
