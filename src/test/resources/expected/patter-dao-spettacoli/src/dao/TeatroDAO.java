package dao;

public interface TeatroDAO {

	// --- CRUD -------------

	public void create(TeatroDTO teatro);

	public TeatroDTO read(String nome);

	public boolean update(TeatroDTO teatro);

	public boolean delete(String nome);

	// ----------------------------------

	public boolean createTable();

	public boolean dropTable();

	// ----------------------------------

	public String metodo();
}
