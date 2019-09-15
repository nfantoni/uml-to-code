package dao.db2;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

{$import-dao-class}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class Db2{$entity-name}DAO implements {$entity-name}DAO {

        Logger logger = Logger.getLogger(getClass().getCanonicalName());

{$initializer}
        // == STATEMENT SQL
        // ====================================================================

        // CREATE TABLE

        static String create = "{$sql-create}";

        // DROP TABLE

        static String drop = "{$sql-drop}";

        // INSERT ELEMENT

        static String insert = "{$sql-insert}";

        // DELETE ELEMENT

        static String delete = "{$sql-delete}";

        // SELECT ELEMENT

        static String read_by_id = "{$sql-read-by-id}";

        // QUERY ELEMENT

        static String query = "{$sql-read}";

        // UPDATE ELEMENT

        static String update = "{$sql-update}";

        @Override
        public void create({$entity-name}DTO {$entity-name-lower}) {
            Connection conn = Db2DAOFactory.createConnection();
            try {
                PreparedStatement prep_stmt = conn.prepareStatement(insert);
                prep_stmt.clearParameters();
{$prepared-create}
                prep_stmt.executeUpdate();
                prep_stmt.close();
            } catch (Exception e) {
                System.out.println("create(): failed to insert entry: "
                + e.getMessage());
                e.printStackTrace();
            }

        }

    @Override
    public {$entity-name}DTO read({$entity-key-type} {$entity-key}) {
        SpettacoloDTO result = null;
        if (codiceSpettacolo == null) {
            System.out
            .println("read(): cannot read an entry with a negative {$entity-key}");
            return result;
        }
        Connection conn = Db2DAOFactory.createConnection();
        try {
            PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
            prep_stmt.clearParameters();
            prep_stmt.set{$entity-key-type}(1, {$entity-key});
            ResultSet rs = prep_stmt.executeQuery();

            if (rs.next()) {
                {$entity-name}DTO entry = new {$entity-name}DTO();
{$prepared-readbyid}

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
    public boolean update({$entity-name}DTO {$entity-name-lower}) {
        boolean result = false;
        if ({$entity-name-lower} == null) {
            System.out.println("update(): failed to update a null entry");
            return result;
        }
        Connection conn = Db2DAOFactory.createConnection();
        try {
            PreparedStatement prep_stmt = conn.prepareStatement(update);
            prep_stmt.clearParameters();
{$prepared-update}
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
    public boolean delete({$entity-key-type} {$entity-key}) {
        boolean result = false;
        if ({$entity-key} == null) {
            System.out
            .println("delete(): cannot delete an entry with an invalid {$entity-key} ");
            return result;
        }
        Connection conn = Db2DAOFactory.createConnection();
        try {
            PreparedStatement prep_stmt = conn.prepareStatement(delete);
            prep_stmt.clearParameters();
            prep_stmt.set{$entity-key-type}(1, {$entity-key});
            prep_stmt.executeUpdate();
            result = true;
            prep_stmt.close();
        } catch (Exception e) {
            System.out.println("delete(): failed to delete entry with id = "
            + {$entity-key} + ": " + e.getMessage());
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
}
