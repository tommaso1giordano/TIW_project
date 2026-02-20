package TIW_project_JS.Controllers.Teacher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/AssignGrade")
public class AssignGrade extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;

	public AssignGrade()
	{
		super();
	}

	public void init() throws ServletException
	{
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		if(connection == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("error connecting to database");
			return;
		}
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		Integer matricola = ParameterUtils.getIntParameter(request, response, "matricola_studente");
		String votoInput = ParameterUtils.getStringParameter(request, response, "voto");
		
		if(idAppello == null || matricola == null || votoInput == null) return;
		
		HttpSession s = request.getSession();
		Docente docente = (Docente) s.getAttribute("docente");
		try
		{
			//security: check if user has permissions
			if(!appelloBelongsToTeacher(idAppello, docente))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you do not have permission");
				return;
			}
			//security: check that grade is not published
			if(gradePublished(idAppello, matricola))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("grade already published");
				return;
			}
			
			EsameDAO esameDAO = new EsameDAO(connection);
			try
			{
				esameDAO.changeGrade(idAppello, matricola, votoInput);
			}
			catch (Exception e)
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				//response.getWriter().println("Invalid grade");
				response.getWriter().println("Hai inserito un voto non valido.");
				return;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching iscritti by appello");
			return;
		}
	}
	
	private boolean gradePublished(int idAppello, int matricola) throws SQLException {
		EsameDAO esamedao = new EsameDAO(connection);
		return esamedao.gradePublished(idAppello, matricola);
	}

	private boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException {
		AppelloDAO appellodao = new AppelloDAO(connection);
		return appellodao.appelloBelongsToTeacher(id_appello, docente);
	}
	
	public void destroy()
	{
		try
		{
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
