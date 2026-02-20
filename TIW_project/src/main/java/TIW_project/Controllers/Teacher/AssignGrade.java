package TIW_project.Controllers.Teacher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import TIW_project.Beans.Docente;
import TIW_project.DAO.AppelloDAO;
import TIW_project.DAO.EsameDAO;
import TIW_project.utils.ConnectionHandler;
import TIW_project.utils.ParameterUtils;

@WebServlet("/AssignGrade")
public class AssignGrade extends HttpServlet {
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
			throws ServletException, IOException
	{
		if(connection == null) {
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		String voto = ParameterUtils.getStringParameter(request, response, "voto");
		if(voto == null) return;
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;
		Integer matricola = ParameterUtils.getIntParameter(request, response, "matricola");
		if(matricola == null) return;
		
		HttpSession s = request.getSession();
		Docente docente = (Docente) s.getAttribute("docente");
		try
		{
			//security: check if user has permissions
			if(!appelloBelongsToTeacher(idAppello, docente)) {
				response.sendRedirect(request.getServletContext().getContextPath() + "/index.html");
				return;
			}
			//security: check that grade is not published
			if(gradePublished(idAppello, matricola)) {
				response.sendRedirect("Error?errorType=already_published");
				return;
			}
			//check that grade is valid
			if(!isGradeValid(voto)) {
				response.sendRedirect("Error?errorType=grade_not_valid");
				return;
			}
			
			EsameDAO esameDAO = new EsameDAO(connection);
			esameDAO.changeGrade(idAppello, matricola, voto);
		} catch (SQLException e) {
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/IscrittiPage?id_appello="+idAppello+"&sortBy='matricola'&sortOrder='asc";
		response.sendRedirect(path);
	}
	
	private boolean isGradeValid(String voto) throws SQLException {
		EsameDAO esamedao = new EsameDAO(connection);
		return esamedao.isGradeValid(voto);
	}

	private boolean gradePublished(int idAppello, int matricola) throws SQLException {
		EsameDAO esamedao = new EsameDAO(connection);
		return esamedao.gradePublished(idAppello, matricola);
	}

	private boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException {
		AppelloDAO appellodao = new AppelloDAO(connection);
		return appellodao.appelloBelongsToTeacher(id_appello, docente);
	}
}
