package TIW_project_JS.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import TIW_project_JS.Beans.*;

public class StudenteDAO
{
	private Connection connection;
	
	public StudenteDAO(Connection connection)
	{
		this.connection = connection;
	}
	
	public Studente getStudente(int matricola) throws SQLException
	{
		Studente studente;
		String query = ""
				+ "SELECT matricola, nome, cognome, email, id_corso_laurea\n"
				+ "FROM studenti\n"
				+ "WHERE matricola = ?";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, matricola);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) // no results
		{
			return null;
		}
		else
		{
			result.next();
			studente = new Studente
			(
				result.getInt("matricola"),
				result.getString("nome"),
				result.getString("cognome"),
				result.getString("email"),
				result.getInt("id_corso_laurea")
			);
			return studente;
		}
	}
	
	public List<Scaglione> getCourseEnrollment(int matricolaStudente) throws SQLException{
		ArrayList<Scaglione> scaglioni = new ArrayList<>();
		
		Scaglione scaglione;
		String query = "select s.id, i.nome, s.matricola_docente, s.anno\r\n"
				+ "from (iscrizioni as iscr join scaglioni as s) join insegnamenti as i\r\n"
				+ "on iscr.id_scaglione = s.id and i.id = s.id_insegnamento\r\n"
				+ "where iscr.matricola_studente = ?\r\n"
				+ "order by i.nome desc";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, matricolaStudente);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) // no results
		{
			return null;
		}
		else
		{
			while(result.next()) {
				scaglione = new Scaglione
				(
					result.getInt("id"),
					result.getString("nome"),
					result.getInt("matricola_docente"),
					result.getInt("anno")
				);
				scaglioni.add(scaglione);
			}
		}
		
		return scaglioni;
	}
	
	public List<Appello> getExams(int matricolaStudente, int idScaglione) throws SQLException{
		AppelloDAO appelloDao = new AppelloDAO(connection);
		return appelloDao.getAppelliByStudenteScaglione(matricolaStudente, idScaglione);
	}

	public Studente loginStudente(int matricola, String password) throws SQLException {
		String query = "SELECT Matricola, Nome, Cognome, Email, id_corso_laurea FROM Studenti WHERE Matricola = ? AND Password = ? ";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, matricola);
		pstatement.setString(2, password);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst())
			return null;
		else
		{
			result.next();
			return new Studente
			(
				result.getInt("Matricola"),
				result.getString("Nome"),
				result.getString("Cognome"),
				result.getString("Email"),
				result.getInt("id_corso_laurea")
			);
		}
	} 
	
	public List<Studente> getStudentiFromId_verbale(int id_verbale) throws SQLException
	{
		List<Studente> studenti = new ArrayList<>();
		String query = ""
				+ "SELECT Matricola, Nome, Cognome, Email, id_corso_laurea\n"
				+ "FROM Studenti\n"
				+ "JOIN studenti_verbale ON studenti.matricola = studenti_verbale.matricola_studente\n"
				+ "WHERE studenti_verbale.id_verbale = ?";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, id_verbale);
		ResultSet result = pstatement.executeQuery();	
		if (!result.isBeforeFirst())
		{
			System.out.println("No studenti in questo verbale");
			return null;
		}
			
		else
		{
			while(result.next())
			{
				int matricola = result.getInt("matricola");
				String nome = result.getString("nome");
				String cognome = result.getString("cognome");
				String email = result.getString("email");
				int id_corso_laurea= result.getInt("id_corso_laurea");
				
				studenti.add(new Studente(matricola, nome, cognome, email, id_corso_laurea));
			}
			
			return studenti;
		}
	}
}