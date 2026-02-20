package TIW_project_JS.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import TIW_project_JS.Beans.Studente;

public class IscrizioniDAO
{
	private Connection connection;
	
	public IscrizioniDAO(Connection connection)
	{
		this.connection = connection;
	}

	public boolean isStudenteInScaglione(int idScaglione, Studente studente) throws SQLException
	{
		String query = "select id\r\n"
				+ "from iscrizioni\r\n"
				+ "where id_scaglione = ? and matricola_studente = ? ";
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, idScaglione);
		pstatement.setInt(2, studente.getMatricola());
		ResultSet result = pstatement.executeQuery();
		return result.isBeforeFirst();
	}
}