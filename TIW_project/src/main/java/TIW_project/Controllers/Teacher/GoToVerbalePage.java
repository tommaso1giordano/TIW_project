package TIW_project.Controllers.Teacher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
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

import TIW_project.Beans.Studente;
import TIW_project.Beans.Verbale;
import TIW_project.utils.ConnectionHandler;
import TIW_project.utils.ParameterUtils;
import TIW_project.DAO.StudenteDAO;
import TIW_project.DAO.VerbaleDAO;
import TIW_project.Beans.Docente;

@WebServlet("/VerbalePage")
public class GoToVerbalePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public GoToVerbalePage()
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
		
		String path = "/WEB-INF/VerbalePage.html";
		
		VerbaleDAO verbaleDAO = new VerbaleDAO(connection);
		StudenteDAO studenteDAO = new StudenteDAO(connection);
		
		Verbale verbale;
		HashMap<Studente, String> votiStudenti;
		
		Integer idVerbale = ParameterUtils.getIntParameter(request, response, "id_verbale");
		if(idVerbale == null) return;
		
		try
		{
			//security: check if user has permissions
			HttpSession s = request.getSession();
			Docente docente = (Docente) s.getAttribute("docente");
			if(!verbaleBelongsToTeacher(idVerbale, docente))
			{
				response.sendRedirect("Error?errorType=forbidden");
				return;
			}
			
			//HashMap from Studente to Integer (voto)
			votiStudenti = studenteDAO.getVotiStudentiFromId_verbale(idVerbale);
			
			//verbale used to get info about date, subject, id
			verbale = verbaleDAO.getVerbale(idVerbale);

			if (votiStudenti.size() == 0)
			{
				String ctxpath = getServletContext().getContextPath();
				path = ctxpath + "/IscrittiPage?id_appello="+verbale.getId_appello();
				response.sendRedirect(path);
				return;
			}
		}
		catch (SQLException e)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}

		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("verbale", verbale);
		ctx.setVariable("votiStudenti", votiStudenti);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	private boolean verbaleBelongsToTeacher(int idVerbale, Docente docente) throws SQLException
	{
		VerbaleDAO verbaledao = new VerbaleDAO(connection);
		return verbaledao.verbaleBelongsToTeacher(idVerbale, docente);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}