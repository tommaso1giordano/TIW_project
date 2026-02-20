package TIW_project_JS.Controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.ConnectionHandler;
import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Beans.Studente;
import TIW_project_JS.DAO.DocenteDAO;
import TIW_project_JS.DAO.StudenteDAO;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public CheckLogin() {
		super();
	}

	public void init() throws ServletException
	{
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		if(connection == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("error connecting to database");
			return;
		}
		
		Integer matricola = ParameterUtils.getIntParameter(request, response, "Matricola");
		if(matricola == null) return;
		String password = ParameterUtils.getStringParameter(request, response, "Password");
		if(password == null) return;
		
		DocenteDAO docenteDAO = new DocenteDAO(connection);
		StudenteDAO studenteDAO = new StudenteDAO(connection);
		Docente docente = null;
		Studente studente = null;
		try
		{
			docente = docenteDAO.loginDocente(matricola, password);
			studente = studenteDAO.loginStudente(matricola, password);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		String path = getServletContext().getContextPath();
		
		if(docente != null) {
			request.getSession().setAttribute("docente", docente);
			String target = "/HomepageDocente";
			path = path + target;
		} else if(studente != null) {
			request.getSession().setAttribute("studente", studente);
			String target = "/HomepageStudente";
			path = path + target;
		}else{
			path = getServletContext().getContextPath() + "/index.html";
		}

		response.sendRedirect(path);
	}

	public void destroy()
	{
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqle) {
		}
	}
}