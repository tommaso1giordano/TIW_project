package TIW_project_JS.Controllers.Teacher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Utils.ConnectionHandler;
import TIW_project_JS.Utils.WebSender;

@WebServlet("/GetDocente")
public class GetDocente extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;
	private WebSender webSender;

	public GetDocente()
	{
		super();
		webSender = new WebSender();
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
		
		Docente docente;
		
		docente = (Docente) request.getSession().getAttribute("docente");
		
		webSender.sendObject(response, docente);
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
