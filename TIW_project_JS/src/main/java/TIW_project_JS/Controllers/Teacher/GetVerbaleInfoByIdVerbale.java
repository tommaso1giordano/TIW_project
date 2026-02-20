package TIW_project_JS.Controllers.Teacher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Beans.Esame;
import TIW_project_JS.Beans.Verbale;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.DAO.VerbaleDAO;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/GetVerbaleInfoByIdVerbale")
public class GetVerbaleInfoByIdVerbale extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;
	private WebSender webSender;

	public GetVerbaleInfoByIdVerbale()
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
		
		VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
		EsameDAO esameDAO = new EsameDAO(connection);
		VerbaleInfo verbaleInfo;
		Verbale verbale;
		List<Esame> esami;
		
		Integer idVerbale = ParameterUtils.getIntParameter(request, response, "id_verbale");
		if(idVerbale == null) return;

		try
		{
			//security: check if user has permissions
			HttpSession s = request.getSession();
			Docente docente = (Docente) s.getAttribute("docente");
			if(!verbaleBelongsToTeacher(idVerbale, docente))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you do not have permission");
				return;
			}
			
			verbale = verbaleDAO.getVerbale(idVerbale);
			esami = esameDAO.getEsamiByIdVerbale(idVerbale);
			
			verbaleInfo = new VerbaleInfo(verbale, esami);
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching verbale infos by idVerbale");
			return;
		}
		
		webSender.sendObject(response, verbaleInfo);
	}
	
	private boolean verbaleBelongsToTeacher(int idVerbale, Docente docente) throws SQLException
	{
		VerbaleDAO verbaledao = new VerbaleDAO(connection);
		return verbaledao.verbaleBelongsToTeacher(idVerbale, docente);
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

// class useful to pass as JSON file
class VerbaleInfo
{
	@SuppressWarnings("unused")
	private Verbale verbale;
	@SuppressWarnings("unused")
	private List<Esame> esami;
	
	public VerbaleInfo(Verbale verbale, List<Esame> esami)
	{
		this.verbale = verbale;
		this.esami = esami;
	}
}