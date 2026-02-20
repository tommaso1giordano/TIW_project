package TIW_project.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import TIW_project.Beans.*;

public class DocenteDAO
{
	private Connection connection;

	public DocenteDAO(Connection connection)
	{
		this.connection = connection;
	}
	
	public Docente getDocenteByMatricola(int matricola) throws SQLException
	{
		String query = "SELECT Matricola, Nome, Cognome, Email FROM Docenti WHERE Matricola = ? ";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, matricola);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) // no results, credential check failed
		{
			return null;
		}
		else
		{
			result.next();
			return new Docente
			(
				result.getInt("Matricola"),
				result.getString("Nome"),
				result.getString("Cognome"),
				result.getString("Email")
			);
		}
	}

	public Docente loginDocente (int matricola, String password) throws SQLException
	{
		Docente docente;
		String query = "SELECT Matricola, Nome, Cognome, Email FROM Docenti WHERE Matricola = ? AND Password = ? ";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, matricola);
		pstatement.setString(2, password);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) // no results, credential check failed
			return null;
		else
		{
			result.next();
			docente = new Docente
			(
				result.getInt("Matricola"),
				result.getString("Nome"),
				result.getString("Cognome"),
				result.getString("Email")
			);
			return docente;
		}
	}
	
	public List<Scaglione> getScaglioni(int matricola) throws SQLException
	{
		List<Scaglione> scaglioni = new ArrayList<>();
		ScaglioneDAO scaglioneDAO = new ScaglioneDAO(connection);
		
		scaglioni = scaglioneDAO.getScaglioniByMatricolaDocente(matricola);
		
		return scaglioni;
	}
	
	public Scaglione getDefaultScaglione(int matricola) throws SQLException
	{
		List<Scaglione> scaglioni;
		Scaglione scaglione; 
	
		scaglioni = getScaglioni(matricola);
		scaglione = null;
		
		if (!scaglioni.isEmpty()) {{scaglione = scaglioni.get(scaglioni.size()-1);}}
		
		return scaglione;
		
	}
}