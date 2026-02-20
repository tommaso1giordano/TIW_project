package TIW_project.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import TIW_project.Beans.Laurea;

public class LaureaDAO 
{
	private Connection connection;
	
	public LaureaDAO(Connection connection)
	{
		this.connection = connection;
	}
	
	public Laurea getLaurea(int id) throws SQLException
	{
		Laurea laurea;
		String query = ""
				+ "SELECT id, nome, tipo_laurea\n"
				+ "FROM lauree\n"
				+ "WHERE id = ?";
		
		PreparedStatement pstatement = connection.prepareStatement(query);
		pstatement.setInt(1, id);
		ResultSet result = pstatement.executeQuery();
		if (!result.isBeforeFirst()) // no results
		{
			return null;
		}
		else
		{
			result.next();
			laurea = new Laurea
			(
				result.getInt("id"),
				result.getString("nome"),
				result.getString("tipo_laurea")
			);
			return laurea;
		}
	}
}
