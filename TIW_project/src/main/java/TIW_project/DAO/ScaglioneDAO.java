package TIW_project.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import TIW_project.Beans.*;

public class ScaglioneDAO
{
	private Connection connection;

	public ScaglioneDAO(Connection connection)
	{
		this.connection = connection;
	}

	public Scaglione getScaglioneById(int id) throws SQLException
	{
		String query = "select s.id, nome, matricola_docente, anno\r\n"
				+ "from scaglioni s join insegnamenti i\r\n"
				+ "on s.id_insegnamento=i.id\r\n"
				+ "where s.id = ?";
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, id);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst())
			return null;
		else
		{
			result.next();
			return new Scaglione
			(
				result.getInt("id"),
				result.getString("nome"),
				result.getInt("matricola_docente"),
				result.getInt("anno")
			);
		}
	}
	
	public List<Scaglione> getScaglioniByMatricolaDocente(int matricola) throws SQLException
	{
		List<Scaglione> scaglioni = new ArrayList<>();
		String query = ""
				+ "SELECT scaglioni_docente.*\n"
				+ "FROM scaglioni_docente\n"
				+ "WHERE matricola_docente = ?;";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, matricola);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next())
				{
					int id = result.getInt("id_scaglione");
					int matricola_docente = result.getInt("matricola_docente");
					String nome_insegnamento = result.getString("nome_insegnamento");
					int anno = result.getInt("anno");
					Scaglione scaglione = new Scaglione(id, nome_insegnamento, matricola_docente, anno);
					scaglioni.add(scaglione);
				}
			}
		}
		return scaglioni;
	}

	public boolean scaglioneBelongsToTeacher(int id_scaglione, Docente docente) throws SQLException
	{
		String query = "select id\r\n"
				+ "from scaglioni\r\n"
				+ "where id=? and matricola_docente=?";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, id_scaglione);
		pstatement.setInt(2, docente.getMatricola());
		ResultSet result = pstatement.executeQuery();
		return result.isBeforeFirst();
	}
}