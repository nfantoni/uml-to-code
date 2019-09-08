package dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import dao.SpettacoloDTO;
import dao.TeatroDAO;
import dao.TeatroDTO;

public class Db2TeatroDAO implements TeatroDAO {

	// Definisco nome tabella

	static final String TABLE = "TEATRO";

	// Definisco parametri tabella

	static final String NOME = "NOME";
	static final String INDIRIZZO = "INDIRIZZO";
	static final String CAPIENZA = "CAPIENZA";

	// == STATEMENT SQL
	// ====================================================================

	// CREATE TABLE

	static String create = "CREATE TABLE TEATRO ( NOME VARCHAR(10) NOT NULL PRIMARY KEY , INDIRIZZO VARCHAR(10) NOT NULL , CAPIENZA INT NOT NULL ) ";

	// DROP TABLE

	static String drop = "DROP TABLE TEATRO";

	// INSERT ELEMENT

	static String insert = "INSERT INTO TEATRO (NOME , INDIRIZZO , CAPIENZA ) VALUES (?,?,?) ";

	// DELETE ELEMENT

	static String delete = "DELETE FROM TEATRO WHERE NOME = ?";

	// SELECT ELEMENT

	static String read_by_id = "SELECT * FROM TEATRO WHERE NOME = ?";

	// QUERY ELEMENT

	static String query = "SELECT * FROM TEATRO";

	// UPDATE ELEMENT

	static String update = "UPDATE TEATRO SET NOME = ?, INDIRIZZO = ?, CAPIENZA = ? WHERE NOME = ?";

	@Override
	public void create(TeatroDTO teatro) {
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, teatro.getNome());
			prep_stmt.setString(2, teatro.getIndirizzo());
			prep_stmt.setInt(3, teatro.getCapienza());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("create(): failed to insert entry: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public TeatroDTO read(String nome) {
		TeatroDTO result = null;
		if (nome == null) {
			System.out
					.println("read(): cannot read an entry with a negative codiceSpettacolo");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, nome);
			ResultSet rs = prep_stmt.executeQuery();

			if (rs.next()) {
				TeatroDTO entry = new TeatroDTO();
				entry.setNome(rs.getString(NOME));
				entry.setIndirizzo(rs.getString(INDIRIZZO));
				entry.setCapienza(rs.getInt(CAPIENZA));

				result = entry;
			}

			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("read(): failed to retrieve entry with id = "
					+ nome + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean update(TeatroDTO teatro) {
		boolean result = false;
		if (teatro == null) {
			System.out.println("update(): failed to update a null entry");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, teatro.getNome());
			prep_stmt.setString(2, teatro.getIndirizzo());
			prep_stmt.setInt(3, teatro.getCapienza());

			prep_stmt.setString(6, teatro.getNome());
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
	public boolean delete(String nome) {
		boolean result = false;
		if (nome == null) {
			System.out
					.println("delete(): cannot delete an entry with an invalid codiceSpettacolo ");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, nome);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.out.println("delete(): failed to delete entry with id = "
					+ nome + ": " + e.getMessage());
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
