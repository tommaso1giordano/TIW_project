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
import TIW_project_JS.Beans.Verbale;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.DAO.VerbaleDAO;
import TIW_project_JS.Exceptions.NoExamsToVerbalizeException;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/Verbalizza")
public class Verbalizza extends HttpServlet
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
		
		EsameDAO esamedao;
		VerbaleDAO verbaleDAO;
		
		InfoCreatedVerbale newVerbale;
		Verbale verbale;
		List<Integer> matricoleDaVerbalizzare;
		
		int numStudentiDaVerbalizzare;
		int idVerbale;
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;
		
		esamedao = new EsameDAO(connection);
		verbaleDAO = new VerbaleDAO(connection);
		
		HttpSession s = request.getSession();
		Docente docente = (Docente) s.getAttribute("docente");
		
		try
		{
			//security: check if user has permissions
			if(!appelloBelongsToTeacher(idAppello, docente))
			{
				response.sendRedirect(request.getServletContext().getContextPath() + "/index.html");
				return;
			}
			
			try
			{
				matricoleDaVerbalizzare = esamedao.getMatricoleDaVerbalizzare(idAppello);
				numStudentiDaVerbalizzare = matricoleDaVerbalizzare.size();
				
				if (numStudentiDaVerbalizzare == 0)
				{
					webSender.sendString(response, "No exams to verbalize");
					return;
				}
				
				//create verbale
				idVerbale = verbaleDAO.createEmptyVerbale(idAppello);
				verbale = verbaleDAO.getVerbale(idVerbale);
				
				verbaleDAO.populateStudentiVerbale(verbale);
				
				newVerbale = new InfoCreatedVerbale(idVerbale, numStudentiDaVerbalizzare);
				
				//verbalizza
				esamedao.adjustRifiutatiGrade(idAppello);
				esamedao.verbalizza(idAppello);
				
				newVerbale = new InfoCreatedVerbale(idVerbale, numStudentiDaVerbalizzare);
				
				webSender.sendObject(response, newVerbale);

				return;
			}
			catch (NoExamsToVerbalizeException e)
			{
				response.getWriter().write("No exams to verbalize");
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

class InfoCreatedVerbale
{
	@SuppressWarnings("unused")
	private int idVerbale;
	@SuppressWarnings("unused")
	private int numStudentiDaVerbalizzare;
	
	public InfoCreatedVerbale(int idVerbale, int numStudentiDaVerbalizzare)
	{
		this.idVerbale = idVerbale;
		this.numStudentiDaVerbalizzare = numStudentiDaVerbalizzare;
	}
}