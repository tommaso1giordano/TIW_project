package TIW_project_JS.Controllers.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.Beans.Studente;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/Rifiuta")
public class Rifiuta extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;

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
		if(idAppello == null) return;
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		HttpSession s = request.getSession();
		Studente studente = (Studente) s.getAttribute("studente");
		EsameDAO esamedao= new EsameDAO(connection);
		try
		{
			//security: check that user has permissions
			if(!studentBelongsToAppello(idAppello, studente)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("this action is now allowed");
				return;
			}
			//security: check if grade can be refused
			if(!gradeCanBeRefused(idAppello, studente)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("this action is not allowed");
				return;
			}
			esamedao.rifiuta(idAppello, studente.getMatricola());
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching appelli by scaglione");
			return;
		}
	}
	
	private boolean gradeCanBeRefused(int idAppello, Studente studente) throws SQLException {
		EsameDAO esamidao = new EsameDAO(connection);
		return esamidao.gradeCanBeRefused(idAppello, studente);
	}
	
	private boolean studentBelongsToAppello(int idAppello, Studente studente) throws SQLException {
		EsameDAO esamidao = new EsameDAO(connection);
		return esamidao.studentBelongsToAppello(idAppello, studente);
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
