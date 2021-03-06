package dao;

public interface SpettacoloDAO {

    // --- CRUD -------------

    public void create(SpettacoloDTO spettacolo);

    public SpettacoloDTO read(String codiceSpettacolo);

    public boolean update(SpettacoloDTO spettacolo);

    public boolean delete(String codiceSpettacolo);

    // ----------------------------------

    public boolean createTable();

    public boolean dropTable();
}
