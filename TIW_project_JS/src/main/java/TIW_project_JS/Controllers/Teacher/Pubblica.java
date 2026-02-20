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
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.Exceptions.NoExamsToPublishException;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/Pubblica")
public class Pubblica extends HttpServlet
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
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{	
		if(connection == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("error connecting to database");
			return;
		}
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if (idAppello == null) return;
		
		HttpSession s = request.getSession();
		Docente docente = (Docente) s.getAttribute("docente");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try
		{
			//security: check if user has permissions
			if(!appelloBelongsToTeacher(idAppello, docente))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you do not have permission");
				return;
			}
			
			EsameDAO esamedao = new EsameDAO(connection);
			
			try
			{
				webSender.sendString(response, Integer.toString(esamedao.pubblica(idAppello)));

				int rowsAffected = esamedao.pubblica(idAppello);
				webSender.sendString(response, Integer.toString(rowsAffected));
				return;
			}
			catch (NoExamsToPublishException e)
			{
				webSender.sendString(response, "No exams to publish");
				return;
			}		
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("error connecting to database");
			return;
		}
	}

	private boolean appelloBelongsToTeacher(int idAppello, Docente docente) throws SQLException
	{
		AppelloDAO appellodao = new AppelloDAO(connection);
		return appellodao.appelloBelongsToTeacher(idAppello, docente);
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
