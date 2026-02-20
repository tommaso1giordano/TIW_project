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

import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Beans.Esame;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/GetEsame")
public class GetEsame extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;
	private WebSender webSender;

	public void init() throws ServletException
	{
		connection = ConnectionHandler.getConnection(getServletContext());
		webSender = new WebSender();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{		
		if(connection == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("error connecting to database");
			return;
		}
		
		EsameDAO esameDAO = new EsameDAO(connection);
		Esame esame;

		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;
		Integer matricolaStudente = ParameterUtils.getIntParameter(request, response, "matricola");
		if(matricolaStudente == null) return;

		try
		{
			//security: check if user has permissions
			HttpSession s = request.getSession();
			Docente docente = (Docente) s.getAttribute("docente");
			if(!appelloBelongsToTeacher(idAppello, docente))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you do not have permission");
				return;
			}
			
			esame = esameDAO.getEsame(matricolaStudente, idAppello);
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching iscritti by appello");
			return;
		}
		
		webSender.sendObject(response, esame);
	}
	
	private boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException
	{
		AppelloDAO appellodao = new AppelloDAO(connection);
		return appellodao.appelloBelongsToTeacher(id_appello, docente);
	}
	
	public void destroy()
	{
		try
		{
			ConnectionHandler.closeConnection(connection);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
