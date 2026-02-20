package TIW_project_JS.Controllers.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import TIW_project_JS.Beans.Studente;
import TIW_project_JS.Utils.ConnectionHandler;
import TIW_project_JS.Utils.WebSender;

@WebServlet("/GetStudente")
public class GetStudente extends HttpServlet
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
		
		Studente studente;
		
		studente = (Studente) request.getSession().getAttribute("studente");
		
		webSender.sendObject(response, studente);
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
