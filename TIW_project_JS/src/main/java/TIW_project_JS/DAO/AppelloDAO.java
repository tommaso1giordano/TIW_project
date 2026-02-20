package TIW_project_JS.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import TIW_project_JS.Beans.*;

import java.sql.Date;

public class AppelloDAO
{
	private Connection connection;
	
	public AppelloDAO(Connection connection)
	{
		this.connection = connection;
	}
	
	public List<Appello> getAppelliByScaglione(int id_scaglione) throws SQLException
	{
		List<Appello> appelli = new ArrayList<>();
		Appello appello;
		String query = "SELECT id, data FROM appelli WHERE id_scaglioni = ? ";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, id_scaglione);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next())
				{
					int id = result.getInt("id");
					Date data = result.getDate("data");
					appello = new Appello(id, id_scaglione, data);
					appelli.add(appello);
				}
			}
		}
		
		
		return appelli;
	}
	
	public List<Appello> getAppelliByStudenteScaglione(int matricola, int idScaglione) throws SQLException
	{
		List<Appello> appelli = new ArrayList<>();
		Appello appello;
		String query = "select a.id, a.id_scaglioni, a.data\r\n"
				+ "from esami e join appelli a\r\n"
				+ "on e.id_appello = a.id\r\n"
				+ "where e.matricola_studente = ? and a.id_scaglioni = ?\r\n"
				+ "order by a.data desc";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, matricola);
		pstatement.setInt(2, idScaglione);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) // no results
		{
			return null;
		}
		else
		{
			while(result.next()) {
				appello = new Appello
				(
					result.getInt("id"),
					result.getInt("id_scaglioni"),
					result.getDate("data")
				);
				appelli.add(appello);
			}
		}
		
		return appelli;
	}
	
	public Appello getAppelloById(int id) throws SQLException
	{
		String query = "select id, id_scaglioni, data\r\n"
				+ "from appelli\r\n"
				+ "where id = ?";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, id);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst())
			return null;
		else
		{
			result.next();
			return new Appello(result.getInt("id"), result.getInt("id_scaglioni"), result.getDate("data"));
		}
	}

	public boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException {
		String query = "select matricola_docente\r\n"
				+ "from appelli a join scaglioni s on a.id_scaglioni = s.id\r\n"
				+ "where a.id = ? and s.matricola_docente = ?";
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, id_appello);
		pstatement.setInt(2, docente.getMatricola());
		ResultSet result = pstatement.executeQuery();
		return result.isBeforeFirst();
	}
}