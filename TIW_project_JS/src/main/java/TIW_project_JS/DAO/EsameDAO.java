package TIW_project_JS.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import TIW_project_JS.Beans.Esame;
import TIW_project_JS.Beans.Studente;
import TIW_project_JS.Exceptions.InvalidParameterException;
import TIW_project_JS.Exceptions.NoExamsToPublishException;
import TIW_project_JS.Exceptions.NoExamsToVerbalizeException;

public class EsameDAO
{
	private Connection connection;
		
	public EsameDAO(Connection connection)
	{
		this.connection = connection;
	}
	
	public Esame getEsame(int matricola_studente, int id_appello) throws SQLException
	{
		String query = ""
				+ "SELECT studenti.nome, studenti.cognome, email, lauree.nome, voto, desc_esito\n"
				+ "FROM esami\n"
				+ "JOIN studenti ON studenti.matricola = esami.matricola_studente\n"
				+ "JOIN lauree ON lauree.id = studenti.id_corso_laurea\n"
				+ "WHERE id_appello = ? AND matricola_studente = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, id_appello);
			pstatement.setInt(2, matricola_studente);
			try (ResultSet result = pstatement.executeQuery();)
			{
				if (result.next())
				{	
					String nome = result.getString("studenti.nome");
					String cognome= result.getString("studenti.cognome");
					String email = result.getString("email");
					String corso_di_laurea = result.getString("lauree.nome");
					String voto = result.getString("voto");
					String desc_esito = result.getString("desc_esito");
					
					return new Esame(id_appello, matricola_studente, nome, cognome,
							email, corso_di_laurea, desc_esito, voto);
				}
				return null;
			}
		}
	}
	
	public Esame getEsamePubblicato(int matricola_studente, int id_appello) throws SQLException
	{
		String query = ""
				+ "SELECT studenti.nome, studenti.cognome, email, lauree.nome, voto, desc_esito\n"
				+ "FROM esami\n"
				+ "JOIN studenti ON studenti.matricola = esami.matricola_studente\n"
				+ "JOIN lauree ON lauree.id = studenti.id_corso_laurea\n"
				+ "WHERE id_appello = ? AND matricola_studente = ? and (esami.desc_esito='Pubblicato' or esami.desc_esito='Rifiutato' or esami.desc_esito='Verbalizzato')";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, id_appello);
			pstatement.setInt(2, matricola_studente);
			try (ResultSet result = pstatement.executeQuery();)
			{
				if (result.next())
				{	
					String nome = result.getString("studenti.nome");
					String cognome= result.getString("studenti.cognome");
					String email = result.getString("email");
					String corso_di_laurea = result.getString("lauree.nome");
					String voto = result.getString("voto");
					String desc_esito = result.getString("desc_esito");
					
					return new Esame(id_appello, matricola_studente, nome, cognome,
							email, corso_di_laurea, desc_esito, voto);
				}
				return null;
			}
		}
	}
		
	public List<Esame> getEsamiByAppello(int id_appello) throws SQLException
	{
		List<Esame> esami = new ArrayList<>();
		Esame esame;
		String query = ""
				+ "SELECT matricola_studente, studenti.nome, studenti.cognome, email, lauree.nome, voto, desc_esito\n"
				+ "FROM esami\n"
				+ "JOIN studenti ON studenti.matricola = esami.matricola_studente\n"
				+ "JOIN lauree ON lauree.id = studenti.id_corso_laurea\n"
				+ "WHERE id_appello = ? ";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, id_appello);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next())
				{					
					int matricola_studente = result.getInt("matricola_studente");
					String nome = result.getString("studenti.nome");
					String cognome= result.getString("studenti.cognome");
					String email = result.getString("email");
					String corso_di_laurea = result.getString("lauree.nome");
					String voto = result.getString("voto");
					String desc_esito = result.getString("desc_esito");
					
					esame = new Esame(id_appello, matricola_studente, nome, cognome, 
							email, corso_di_laurea, desc_esito, voto);
					esami.add(esame);
				}
			}
		}
		return esami;
	}
	
	public List<Esame> getEsamiNonInseritiByAppello(int id_appello) throws SQLException
	{
		List<Esame> esami = new ArrayList<>();
		Esame esame;
		String query = ""
				+ "SELECT matricola_studente, studenti.nome, studenti.cognome, email, lauree.nome, voto, desc_esito\n"
				+ "FROM esami\n"
				+ "JOIN studenti ON studenti.matricola = esami.matricola_studente\n"
				+ "JOIN lauree ON lauree.id = studenti.id_corso_laurea\n"
				+ "WHERE id_appello = ? and desc_esito = 'Non inserito'";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, id_appello);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next())
				{					
					int matricola_studente = result.getInt("matricola_studente");
					String nome = result.getString("studenti.nome");
					String cognome= result.getString("studenti.cognome");
					String email = result.getString("email");
					String corso_di_laurea = result.getString("lauree.nome");
					String voto = result.getString("voto");
					String desc_esito = result.getString("desc_esito");
					
					esame = new Esame(id_appello, matricola_studente, nome, cognome, 
							email, corso_di_laurea, desc_esito, voto);
					esami.add(esame);
				}
			}
		}
		return esami;
	}
	
	public List<Esame> getEsamiByIdVerbale(int id_verbale) throws SQLException
	{
		List<Esame> esami = new ArrayList<>();
		Esame esame;
		String query = "" +
				"SELECT esami.id_appello, esami.matricola_studente AS matricola_studente, desc_esito, voto\n"
				+ "FROM esami\n"
				+ "JOIN studenti_verbale ON studenti_verbale.matricola_studente = esami.matricola_studente\n"
				+ "JOIN verbali ON verbali.id = studenti_verbale.id_verbale\n"
				+ "WHERE id_verbale = ? and verbali.id_appello = esami.id_appello";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, id_verbale);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next())
				{	
					int id_appello = result.getInt("id_appello");
					int matricola_studente = result.getInt("matricola_studente");
					String desc_esito = result.getString("desc_esito");
					String voto = result.getString("voto");
					
					esame = new Esame(id_appello, matricola_studente, desc_esito, voto);
					esami.add(esame);
				}
			}
		}
		return esami;
	}

	public void rifiuta(int idAppello, int matricola) throws SQLException {
		String query = "update esami\r\n"
				+ "set desc_esito = 'Rifiutato'\r\n"
				+ "where id_appello=? and matricola_studente=?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, idAppello);
			pstatement.setInt(2, matricola);
			pstatement.executeUpdate();
		}
	}

	public void changeGrade(int idAppello, int matricola, String voto) throws SQLException, InvalidParameterException
	{
		String query = "update esami\r\n"
				+ "set voto = ?, desc_esito = 'Inserito'\r\n"
				+ "where id_appello=? and matricola_studente=?";
		
		if (isGradeValid(voto))
		{
			try (PreparedStatement pstatement = connection.prepareStatement(query);)
			{
				pstatement.setString(1, voto);
				pstatement.setInt(2, idAppello);
				pstatement.setInt(3, matricola);
				pstatement.executeUpdate();
			}
		}
		else
		{
			throw new InvalidParameterException();
		}
	}
	
	public int pubblica(int idAppello) throws SQLException, NoExamsToPublishException
	{
		String query = "update esami\r\n"
				+ "set desc_esito = 'Pubblicato'\r\n"
				+ "where id_appello = ? and desc_esito = 'Inserito' and voto != '<vuoto>'";
		int rowsAffected = 0;
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, idAppello);
			
			rowsAffected = pstatement.executeUpdate();
			
			if (rowsAffected == 0)
			{
				throw new NoExamsToPublishException();
			}
			return rowsAffected;
		}
	}
	
	public void adjustRifiutatiGrade(int idAppello) throws SQLException
	{
		String query = "update esami\r\n"
				+ "set voto = '<vuoto>'\r\n"
				+ "where id_appello = ? and desc_esito = 'Rifiutato'";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, idAppello);
			pstatement.executeUpdate();
		}
	}
	
	public void verbalizza(int idAppello) throws SQLException, NoExamsToVerbalizeException
	{
		String query = "update esami\r\n"
				+ "set desc_esito = 'Verbalizzato'\r\n"
				+ "where id_appello = ? and (desc_esito = 'Pubblicato' or desc_esito = 'Rifiutato')";
		int rowsAffected = 0;
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			pstatement.setInt(1, idAppello);
			
			rowsAffected = pstatement.executeUpdate();
			
			if (rowsAffected == 0)
			{
				
				throw new NoExamsToVerbalizeException();
			}
		}
	}

	public boolean gradePublished(int idAppello, int matricola) throws SQLException {
		String query = "select voto\r\n"
				+ "from esami\r\n"
				+ "where id_appello=? and matricola_studente = ? and (desc_esito=\"Pubblicato\" or desc_esito=\"Rifiutato\" or desc_esito=\"Verbalizzato\")";
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, idAppello);
		pstatement.setInt(2, matricola);
		ResultSet result = pstatement.executeQuery();
		return result.isBeforeFirst();
	}

	public boolean studentBelongsToAppello(int idAppello, Studente studente) throws SQLException {
		String query = "select id_appello\r\n"
				+ "from esami\r\n"
				+ "where id_appello = ? and matricola_studente = ?";
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, idAppello);
		pstatement.setInt(2, studente.getMatricola());
		ResultSet result = pstatement.executeQuery();
		return result.isBeforeFirst();
	}

	public boolean gradeCanBeRefused(int idAppello, Studente studente) throws SQLException {
		String query = "select id_appello\r\n"
				+ "from esami\r\n"
				+ "where id_appello = ? and matricola_studente = ? and desc_esito = \"Pubblicato\"\r\n"
				+ "and voto != 'Assente' and voto != 'Rimandato' and voto != 'Riprovato' and voto != '<vuoto>'";
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, idAppello);
		pstatement.setInt(2, studente.getMatricola());
		ResultSet result = pstatement.executeQuery();
		return result.isBeforeFirst();
	}
	
	private boolean isGradeValid(String grade) throws SQLException
	{
		String query = ""
				+ "SELECT * FROM voti";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{
			try (ResultSet result = pstatement.executeQuery();)
			{
				while (result.next())
				{					
					if (result.getString("desc").equals(grade)) return true;
				}
				return false;
			}
		}	
	}

	public List<Integer> getMatricoleDaVerbalizzare(int id_appello) throws SQLException{
		String query = ""
				+ "SELECT matricola_studente\n"
				+ "FROM esami\n"
				+ "WHERE (desc_esito = 'Pubblicato' or desc_esito = 'Rifiutato') and id_appello = ? and voto != '<vuoto>'";
		
		List<Integer> matricole = new ArrayList<>();
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);)
		{	
			
			pstatement.setInt(1, id_appello);                          
			
			try (ResultSet result = pstatement.executeQuery();)
			{
				while (result.next())
				{	
					int matricola_studente = result.getInt("matricola_studente");

					matricole.add(matricola_studente);
				}
	
				return matricole;
			}
		}
	}
}