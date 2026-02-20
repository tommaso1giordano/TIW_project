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

import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Beans.Verbale;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.DAO.VerbaleDAO;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/GetVerbaliByAppello")
public class GetVerbaliByAppello extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private Connection connection = null;
	private WebSender webSender;
	
	public GetVerbaliByAppello()
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
		List<Verbale> verbali = new ArrayList<Verbale>();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;

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
			
			verbali = verbaleDAO.getVerbaliById_appello(idAppello);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching verbali by appello");
			return;
		}
		
		webSender.sendObject(response, verbali);
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
