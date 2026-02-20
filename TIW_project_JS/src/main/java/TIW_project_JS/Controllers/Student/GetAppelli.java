package TIW_project_JS.Controllers.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project_JS.DAO.IscrizioniDAO;
import TIW_project_JS.Beans.Appello;
import TIW_project_JS.Beans.Studente;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.StudenteDAO;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/GetAppelli")
public class GetAppelli extends HttpServlet
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
		
		StudenteDAO studenteDAO = new StudenteDAO(connection);
		List<Appello> appelli = new ArrayList<Appello>();
		
		Integer idScaglione = ParameterUtils.getIntParameter(request, response, "id_scaglione");
		if(idScaglione == null) return;

		try
		{
			//security: check that user has permissions
			HttpSession s = request.getSession();
			Studente studente = (Studente) s.getAttribute("studente");
			if(!studentBelongsToScaglione(idScaglione, studente)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you cannot access this data");
				return;
			}
			
			appelli = (ArrayList<Appello>) studenteDAO.getExams(studente.getMatricola(), idScaglione);
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching appelli by scaglione");
			return;
		}
		
		webSender.sendObject(response,  appelli);
	}
	
	private boolean studentBelongsToScaglione(int idScaglione, Studente studente) throws SQLException {
		IscrizioniDAO iscrizionidao = new IscrizioniDAO(connection);
		return iscrizionidao.isStudenteInScaglione(idScaglione, studente);
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
