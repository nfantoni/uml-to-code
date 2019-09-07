package dao;

import java.util.Date;

public class SpettacoloDTO {

	private String codiceSpettacolo;
	private String nomeArtista;
	private long data;
	private String genere;
	private String nomeTeatro;

	public SpettacoloDTO() {

	}

	public String getCodiceSpettacolo() {
		return codiceSpettacolo;
	}

	public void setCodiceSpettacolo(String codiceSpettacolo) {
		this.codiceSpettacolo = codiceSpettacolo;
	}

	public String getNomeArtista() {
		return nomeArtista;
	}

	public void setNomeArtista(String nomeArtista) {
		this.nomeArtista = nomeArtista;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getNomeTeatro() {
		return nomeTeatro;
	}

	public void setNomeTeatro(String nomeTeatro) {
		this.nomeTeatro = nomeTeatro;
	}
}
