package TIW_project.Controllers.Teacher;

import java.io.IOException;

import java.sql.SQLException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import TIW_project.Beans.Docente;
import TIW_project.Beans.Esame;
import TIW_project.Beans.Laurea;
import TIW_project.Beans.Studente;
import TIW_project.utils.ConnectionHandler;
import TIW_project.utils.ParameterUtils;
import TIW_project.DAO.AppelloDAO;
import TIW_project.DAO.EsameDAO;
import TIW_project.DAO.LaureaDAO;
import TIW_project.DAO.StudenteDAO;

@WebServlet("/GoToFormEsame")
public class GoToFormEsame extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

	public GoToFormEsame()
	{
		super();
	}

	public void init() throws ServletException
	{
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		if(connection == null) {
			response.sendRedirect("Error?errorType=sql");
			return;
		}
	
		String path = "/WEB-INF/FormEsame.html";
		
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
			
			EsameDAO esameDAO = new EsameDAO(connection);
			StudenteDAO studenteDAO = new StudenteDAO(connection);
			LaureaDAO laureaDAO = new LaureaDAO(connection);
			
			Esame esame = esameDAO.getEsame(matricola, idAppello);
			Studente studente = studenteDAO.getStudente(matricola);
			int id_corso_laurea = studente.getId_corso_laurea();
			Laurea laurea = laureaDAO.getLaurea(id_corso_laurea);

			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			ctx.setVariable("esame", esame);
			ctx.setVariable("studente", studente);
			ctx.setVariable("laurea", laurea);
			
			templateEngine.process(path, ctx, response.getWriter());
		}
		catch (SQLException sqle)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}
	}

	private boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException {
		AppelloDAO appellodao = new AppelloDAO(connection);
		return appellodao.appelloBelongsToTeacher(id_appello, docente);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}
}