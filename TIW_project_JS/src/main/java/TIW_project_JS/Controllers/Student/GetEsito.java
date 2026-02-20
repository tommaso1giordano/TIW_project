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

import TIW_project_JS.Beans.Appello;
import TIW_project_JS.Beans.Docente;
import TIW_project_JS.Beans.Esame;
import TIW_project_JS.Beans.Scaglione;
import TIW_project_JS.DAO.AppelloDAO;
import TIW_project_JS.DAO.DocenteDAO;
import TIW_project_JS.DAO.EsameDAO;
import TIW_project_JS.DAO.ScaglioneDAO;
import TIW_project_JS.Beans.Studente;
import TIW_project_JS.Utils.ParameterUtils;
import TIW_project_JS.Utils.WebSender;
import TIW_project_JS.Utils.ConnectionHandler;

@WebServlet("/GetEsito")
public class GetEsito extends HttpServlet
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
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;
		
		HttpSession s = request.getSession();
		Studente studente = (Studente) s.getAttribute("studente");
		int matricola = studente.getMatricola();
		
		EsameDAO esamedao = new EsameDAO(connection);
		AppelloDAO appellodao = new AppelloDAO(connection);
		ScaglioneDAO scaglionedao = new ScaglioneDAO(connection);
		DocenteDAO docentedao = new DocenteDAO(connection);
		Esame esame = null;
		Appello appello = null;
		Scaglione scaglione = null;
		Docente docente = null;
		
		try
		{
			//security: check that user has permissions
			if(!studentBelongsToAppello(idAppello, studente) || matricola != studente.getMatricola()) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("you cannot access this data");
				return;
			}
			
			esame = esamedao.getEsamePubblicato(matricola, idAppello);
			if(esame == null) {
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.getWriter().println("Voto non ancora definito");
				return;
			}
			
			appello = appellodao.getAppelloById(idAppello);
			scaglione = scaglionedao.getScaglioneById(appello.getId_scaglione());
			docente = docentedao.getDocenteByMatricola(scaglione.getMatricola_docente());
			
			ExamInfo examInfo = new ExamInfo(
					scaglione.getNome_insegnamento(),
					docente.getNome() + " " + docente.getCognome(),
					appello.getDate(),
					esame.getVoto(),
					esame.getDesc_esito(),
					gradeCanBeRefused(idAppello, studente)
				);
			
			webSender.sendObject(response, examInfo);
		}
		catch (SQLException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("error connecting to database");
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
