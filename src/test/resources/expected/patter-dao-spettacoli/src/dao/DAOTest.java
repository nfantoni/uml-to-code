package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class DAOTest {

	public static final int DAO = DAOFactory.DB2;

	public static void main(String[] args) {

		DAOFactory daoFactoryInstance = DAOFactory.getDAOFactory(DAO);

		// creo tabella Teatro
		TeatroDAO teatroDAO = daoFactoryInstance.getTeatroDAO();
		teatroDAO.dropTable();
		teatroDAO.createTable();

		// creo tabella Teatro
		SpettacoloDAO spettacoloDAO = daoFactoryInstance.getSpettacoloDAO();
		spettacoloDAO.dropTable();
		spettacoloDAO.createTable();

		// Creao due istanze di tearto
		TeatroDTO t1 = new TeatroDTO();
		t1.setNome("teatro1");
		t1.setIndirizzo("indir1");
		t1.setCapienza(20);
		teatroDAO.create(t1);

		TeatroDTO t2 = new TeatroDTO();
		t2.setNome("teatro2");
		t2.setIndirizzo("indir2");
		t2.setCapienza(30);
		teatroDAO.create(t2);

		TeatroDTO t3 = new TeatroDTO();
		t3.setNome("teatro3");
		t3.setIndirizzo("indir3");
		t3.setCapienza(50);
		teatroDAO.create(t3);

		// Creo tre spettacoli di uno stesso genere tenuti nello stesso teatro
		// in date diverse
		SpettacoloDTO s1 = new SpettacoloDTO();
		s1.setCodiceSpettacolo("cod1");
		s1.setData(1);
		s1.setGenere("balletto");
		s1.setNomeArtista("a1");
		s1.setNomeTeatro("teatro1");
		spettacoloDAO.create(s1);

		SpettacoloDTO s2 = new SpettacoloDTO();
		s2.setCodiceSpettacolo("cod2");
		s2.setData(2);
		s2.setGenere("balletto");
		s2.setNomeArtista("a1");
		s2.setNomeTeatro("teatro1");
		spettacoloDAO.create(s2);

		SpettacoloDTO s3 = new SpettacoloDTO();
		s3.setCodiceSpettacolo("cod3");
		s3.setData(3);
		s3.setGenere("g1");
		s3.setNomeArtista("a1");
		s3.setNomeTeatro("teatro1");
		spettacoloDAO.create(s3);

		// Creo tre spettacoli di genere diverso tenuti in teatri diversi anche
		// nella stessa data

		SpettacoloDTO s4 = new SpettacoloDTO();
		s4.setCodiceSpettacolo("cod4");
		s4.setData(1);
		s4.setGenere("balletto");
		s4.setNomeArtista("a1");
		s4.setNomeTeatro("teatro1");
		spettacoloDAO.create(s4);

		SpettacoloDTO s5 = new SpettacoloDTO();
		s5.setCodiceSpettacolo("cod5");
		s5.setData(1);
		s5.setGenere("balletto");
		s5.setNomeArtista("a1");
		s5.setNomeTeatro("teatro2");
		spettacoloDAO.create(s5);

		SpettacoloDTO s6 = new SpettacoloDTO();
		s6.setCodiceSpettacolo("cod6");
		s6.setData(1);
		s6.setGenere("g6");
		s6.setNomeArtista("a1");
		s6.setNomeTeatro("teatro3");
		spettacoloDAO.create(s6);

		StringBuilder builder = new StringBuilder();

		builder.append("Il nome del teatro in cui si sono tenuti più spettacoli di genere balletto è: "
				+ getNome(teatroDAO));

		List<String> generi = getGeneri(spettacoloDAO);

		builder.append("\n\nL’elenco distinto dei generi presenti nella programmazione teatrale è:");

		for (String g : generi) {
			builder.append("\n" + g);
		}

		System.out.println(builder.toString());

		File f = new File("Spettacolo.txt");
		try {
			if (!f.exists()) {
				f.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(f);
			PrintWriter out = new PrintWriter(fos);
			out.write(builder.toString());
			out.close();
			System.out.println("Written to file!");
		} catch (Exception e) {
		}
	}

	private static String getNome(TeatroDAO t) {
		return t.metodo();
	}

	private static List<String> getGeneri(SpettacoloDAO s) {
		return s.metodo();
	}
}
