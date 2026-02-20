package TIW_project.Controllers.Teacher;

import java.io.IOException;
import java.util.List;

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

import TIW_project.Beans.*;
import TIW_project.DAO.*;
import TIW_project.utils.ConnectionHandler;

@WebServlet("/HomepageDocente")
public class GoToTeacherHP extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	
	private Docente docente;
	private DocenteDAO docenteDAO;
	private List<Scaglione> scaglioni;
	private List<Appello> appelli;
	private boolean isScaglioneEmpty;
	private boolean isAppelliEmpty;

	public GoToTeacherHP()
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
		
		String path = "/WEB-INF/HomepageDocente.html";
		
		HttpSession s = request.getSession();
		
		docente = (Docente) s.getAttribute("docente");
		
		docenteDAO = new DocenteDAO(connection);
		AppelloDAO appelloDAO = new AppelloDAO(connection);
		
		String inputScaglione = request.getParameter("id_scaglione");
		int id_scaglione;
		
		try
		{
			scaglioni = docenteDAO.getScaglioni(docente.getMatricola());
			if (!scaglioni.isEmpty())
			{
				isScaglioneEmpty = false;
				
				if(inputScaglione == null)
				{
					id_scaglione = docenteDAO.getDefaultScaglione(docente.getMatricola()).getId();
				}
				else
				{
					try
					{
						id_scaglione = Integer.parseInt(inputScaglione);
					}
					catch(NumberFormatException e)
					{
						response.sendRedirect("Error?errorType=not_a_number");
						return;
					}
				}
				
				//security: check that user has permissions
				if(!scaglioneBelongsToTeacher(id_scaglione, docente))
				{
					response.sendRedirect(request.getServletContext().getContextPath() + "/index.html");
					return;
				}
				appelli = appelloDAO.getAppelliByScaglione(id_scaglione);
				
				if (!appelli.isEmpty()) isAppelliEmpty = false;
				else isAppelliEmpty = true;
			}
			else
			{
				isScaglioneEmpty = true;
				id_scaglione = 0;
			}
		}
		catch (SQLException e)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("isScaglioniEmpty", isScaglioneEmpty);
		ctx.setVariable("isAppelliEmpty", isAppelliEmpty);
		ctx.setVariable("scaglioni", scaglioni);
		ctx.setVariable("docente", docente);
		ctx.setVariable("scaglione_id", id_scaglione);
		ctx.setVariable("appelli", appelli);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	private boolean scaglioneBelongsToTeacher(int id_scaglione, Docente docente) throws SQLException
	{
		ScaglioneDAO scaglionedao = new ScaglioneDAO(connection);
		return scaglionedao.scaglioneBelongsToTeacher(id_scaglione, docente);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

}
