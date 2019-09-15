package dao.db2;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dao.SpettacoloDAO;
import dao.SpettacoloDTO;
import dao.TeatroDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class Db2SpettacoloDAO implements SpettacoloDAO {

        Logger logger = Logger.getLogger(getClass().getCanonicalName());

        // Definisco nome tabella

        static final String TABLE = "SPETTACOLO";

        // Definisco parametri tabella

        static final String CODICESPETTACOLO = "CODICESPETTACOLO";
        static final String NOMEARTISTA = "NOMEARTISTA";
        static final String DATA = "DATA";
        static final String GENERE = "GENERE";
        static final String NOMETEATRO = "NOMETEATRO";

        // == STATEMENT SQL
        // ====================================================================

        // CREATE TABLE

        static String create = "CREATE TABLE SPETTACOLO ( CODICESPETTACOLO VARCHAR(255) NOT NULL PRIMARY KEY , NOMEARTISTA VARCHAR(255) , DATA DATE , GENERE VARCHAR(255) , NOMETEATRO VARCHAR(255) NOT NULL REFERENCES TEATRO(NOME))";

        // DROP TABLE

        static String drop = "DROP TABLE SPETTACOLO";

        // INSERT ELEMENT

        static String insert = "INSERT INTO SPETTACOLO (CODICESPETTACOLO , NOMEARTISTA , DATA , GENERE , NOMETEATRO ) VALUES (?,?,?,?,?)";

        // DELETE ELEMENT

        static String delete = "DELETE FROM SPETTACOLO WHERE CODICESPETTACOLO = ?";

        // SELECT ELEMENT

        static String read_by_id = "SELECT * FROM SPETTACOLO WHERE CODICESPETTACOLO = ?";

        // QUERY ELEMENT

        static String query = "SELECT * FROM SPETTACOLO";

        // UPDATE ELEMENT

        static String update = "UPDATE SPETTACOLO SET CODICESPETTACOLO = ?, NOMEARTISTA = ?, DATA = ?, GENERE = ?, NOMETEATRO = ? WHERE CODICESPETTACOLO = ?";

        @Override
        public void create(SpettacoloDTO spettacolo) {
            Connection conn = Db2DAOFactory.createConnection();
            try {
                PreparedStatement prep_stmt = conn.prepareStatement(insert);
                prep_stmt.clearParameters();
                prep_stmt.setString(1, spettacolo.getCodiceSpettacolo());
                prep_stmt.setString(2, spettacolo.getNomeArtista());
                prep_stmt.setDate(3, spettacolo.getData());
                prep_stmt.setString(4, spettacolo.getGenere());
                prep_stmt.setString(5, spettacolo.getNomeTeatro());

                prep_stmt.executeUpdate();
                prep_stmt.close();
            } catch (Exception e) {
                System.out.println("create(): failed to insert entry: "
                + e.getMessage());
                e.printStackTrace();
            }

        }

	@Override
	public SpettacoloDTO read(String codiceSpettacolo) {
		SpettacoloDTO result = null;
		if (codiceSpettacolo == null) {
			System.out
					.println("read(): cannot read an entry with a negative codiceSpettacolo");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, codiceSpettacolo);
			ResultSet rs = prep_stmt.executeQuery();

			if (rs.next()) {
				SpettacoloDTO entry = new SpettacoloDTO();
				entry.setCodiceSpettacolo(rs.getString(CODICESPETTACOLO));
				entry.setGenere(rs.getString(GENERE));
				entry.setNomeArtista(rs.getString(NOMEARTISTA));
				entry.setNomeTeatro(rs.getString(NOMETEATRO));
				entry.setData(rs.getDate(DATA));

				result = entry;
			}

			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("read(): failed to retrieve entry with id = "
					+ codiceSpettacolo + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean update(SpettacoloDTO spettacolo) {
		boolean result = false;
		if (spettacolo == null) {
			System.out.println("update(): failed to update a null entry");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, spettacolo.getCodiceSpettacolo());
			prep_stmt.setString(2, spettacolo.getNomeArtista());
			prep_stmt.setDate(3, spettacolo.getData());
			prep_stmt.setString(4, spettacolo.getGenere());
			prep_stmt.setString(5, spettacolo.getNomeTeatro());

			prep_stmt.setString(6, spettacolo.getCodiceSpettacolo());
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("insert(): failed to update entry: "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean delete(String codiceSpettacolo) {
		boolean result = false;
		if (codiceSpettacolo == null) {
			System.out
					.println("delete(): cannot delete an entry with an invalid codiceSpettacolo ");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, codiceSpettacolo);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("delete(): failed to delete entry with id = "
					+ codiceSpettacolo + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean createTable() {
		boolean result = false;
		Connection conn = Db2DAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(create);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.out.println("createTable(): failed to create table '"
					+ TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean dropTable() {
		boolean result = false;
		Connection conn = Db2DAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(drop);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.out.println("dropTable(): failed to drop table '" + TABLE
					+ "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

}

