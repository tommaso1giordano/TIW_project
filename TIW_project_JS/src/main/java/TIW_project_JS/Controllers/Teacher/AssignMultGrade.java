package TIW_project_JS.Controllers.Teacher;

import java.io.IOException;
import java.lang.reflect.Type;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.Exceptions.InvalidParameterException;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/AssignMultGrade")
public class AssignMultGrade extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private WebSender webSender;

	private Connection connection = null;

	public AssignMultGrade()
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
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		String matricoleInput = ParameterUtils.getStringParameter(request, response, "matricole");
		String votoInput = ParameterUtils.getStringParameter(request, response, "voto");
		
		List<Integer> matricole;
		
		if(idAppello == null || votoInput == null || matricoleInput == null) return;
		
		String decodedMatricole;
		try {
			decodedMatricole = java.net.URLDecoder.decode(matricoleInput, "UTF-8");
		}catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Input non correct");
            return;
		}
		
		HttpSession s = request.getSession();
		Docente docente = (Docente) s.getAttribute("docente");
		try
		{
			Gson gson = new GsonBuilder().create();
			
			Type listOfMyClassObject = new TypeToken<ArrayList<Integer>>() {}.getType();
			
			try {
				matricole =  gson.fromJson(decodedMatricole, listOfMyClassObject);
			} catch(Exception e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("Input non correct");
	            return;
			}
			
			//security: check if user has permissions
			if(!appelloBelongsToTeacher(idAppello, docente))
			{
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you do not have permission");
				return;
			}
			
			if (matricole.isEmpty())
			{
				webSender.sendString(response, "No matricole selected");
				return;
			}
			
			EsameDAO esameDAO = new EsameDAO(connection);
			
			//do a first loop to check if all grades can be published
			for (Integer matricola : matricole)
			{
				//security: check that grade is not published
				if(gradePublished(idAppello, matricola))
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("A grade is already published");
					return;
				}
			}
			
			//change grades
			for (Integer matricola : matricole)
			{
				try
				{
					esameDAO.changeGrade(idAppello, matricola, votoInput);
				}
				catch (InvalidParameterException e)
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Hai inserito un voto non valido.");
					return;
				}
			}
		}
		catch(SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error fetching iscritti by appello");
			return;
		}
	}
	
	private boolean gradePublished(int idAppello, int matricola) throws SQLException {
		EsameDAO esamedao = new EsameDAO(connection);
		return esamedao.gradePublished(idAppello, matricola);
	}

	private boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException {
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
