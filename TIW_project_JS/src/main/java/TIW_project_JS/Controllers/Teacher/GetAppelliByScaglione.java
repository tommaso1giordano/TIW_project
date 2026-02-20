package TIW_project_JS.Controllers.Teacher;

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

import TIW_project_JS.Beans.Appello;
import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.DAO.ScaglioneDAO;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/GetAppelliByScaglione")
public class GetAppelliByScaglione extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;
	private WebSender webSender;

	public GetAppelliByScaglione()
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
		
		AppelloDAO appelloDAO = new AppelloDAO(connection);
		List<Appello> appelli = new ArrayList<Appello>();
		
		Integer idScaglione = ParameterUtils.getIntParameter(request, response, "id_scaglione");
		if(idScaglione == null) return;
		
		try
		{
			//security: check that user has permissions
			HttpSession s = request.getSession();
			Docente docente = (Docente) s.getAttribute("docente");
			if(!scaglioneBelongsToTeacher(idScaglione, docente))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you do not have permission");
				return;
			}
	
			appelli = appelloDAO.getAppelliByScaglione(idScaglione);
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching appelli by scaglione");
			return;
		}

		
		
		webSender.sendObject(response, appelli);
	}
	
	private boolean scaglioneBelongsToTeacher(int idScaglione, Docente docente) throws SQLException
	{
		ScaglioneDAO scaglionedao = new ScaglioneDAO(connection);
		return scaglionedao.scaglioneBelongsToTeacher(idScaglione, docente);
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
