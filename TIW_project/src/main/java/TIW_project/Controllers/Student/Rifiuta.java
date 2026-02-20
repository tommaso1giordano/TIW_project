package TIW_project.Controllers.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project.Beans.Studente;
import TIW_project.utils.ConnectionHandler;
import TIW_project.utils.ParameterUtils;
import TIW_project.DAO.EsameDAO;

@WebServlet("/Rifiuta")
public class Rifiuta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(connection == null) {
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;
		
		HttpSession s = request.getSession();
		Studente studente = (Studente) s.getAttribute("studente");
		EsameDAO esamedao= new EsameDAO(connection);
		try
		{
			//security: check that user has permissions
			if(!studentBelongsToAppello(idAppello, studente)) {
				response.sendRedirect(request.getServletContext().getContextPath() + "/index.html");
				return;
			}
			//security: check if grade can be refused
			if(!gradeCanBeRefused(idAppello, studente)) {
				response.sendRedirect("Error?errorType=cannot_be_refused");
				return;
			}
			esamedao.rifiuta(idAppello, studente.getMatricola());
		} catch (SQLException e)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/Esito?id_appello=" + idAppello + "&matricola=" + studente.getMatricola();
		response.sendRedirect(path);
	}

	private boolean gradeCanBeRefused(int idAppello, Studente studente) throws SQLException {
		EsameDAO esamidao = new EsameDAO(connection);
		return esamidao.gradeCanBeRefused(idAppello, studente);
	}

	private boolean studentBelongsToAppello(int idAppello, Studente studente) throws SQLException {
		EsameDAO esamidao = new EsameDAO(connection);
		return esamidao.studentBelongsToAppello(idAppello, studente);
	}
}
