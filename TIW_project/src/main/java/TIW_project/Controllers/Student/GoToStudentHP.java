package TIW_project.Controllers.Student;

import java.io.IOException;
import java.util.ArrayList;

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

@WebServlet("/HomepageStudente")
public class GoToStudentHP extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	
	private Studente studente;
	private StudenteDAO studenteDAO;

	public GoToStudentHP()
	{
		super();
	}

	public void init() throws ServletException {
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
		
		String path = "/WEB-INF/HomepageStudente.html";
		
		HttpSession s = request.getSession();
		
		studente = (Studente) s.getAttribute("studente");
		
		studenteDAO = new StudenteDAO(connection);
		ArrayList<Scaglione> scaglioni = null;
		ArrayList<Appello> appelli = null;
		String chosenScaglione = request.getParameter("id_scaglione");
		int idScaglione = 0;
		boolean idScaglioneValid = false;
		boolean isScaglioniEmpty;
		boolean isAppelliEmpty;
		
		try
		{
			scaglioni = (ArrayList<Scaglione>) studenteDAO.getCourseEnrollment(studente.getMatricola());
			if (chosenScaglione == null)
			{
				if(scaglioni != null && scaglioni.size()>0) {
					idScaglione = scaglioni.get(0).getId();
					idScaglioneValid = true;
				}
			}
			else
			{
				try{
					idScaglione = Integer.parseInt(chosenScaglione);
					idScaglioneValid = true;
				}catch(NumberFormatException e){
					response.sendRedirect("Error?errorType=not_a_number");
					return;
				}
				
			}
			if(idScaglioneValid) {
				//security: check that user has permissions
				if(!studentBelongsToScaglione(idScaglione, studente)) {
					response.sendRedirect(request.getServletContext().getContextPath() + "/index.html");
					return;
				}
				
				appelli = (ArrayList<Appello>) studenteDAO.getExams(studente.getMatricola(), idScaglione);
			}
		} catch (SQLException e)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		if (scaglioni == null || scaglioni.isEmpty()) isScaglioniEmpty = true;
		else isScaglioniEmpty = false;
		
		if (appelli == null || appelli.isEmpty()) isAppelliEmpty = true;
		else isAppelliEmpty = false;

		ctx.setVariable("isScaglioniEmpty", isScaglioniEmpty);
		ctx.setVariable("isAppelliEmpty", isAppelliEmpty);
		ctx.setVariable("scaglioni", scaglioni);
		ctx.setVariable("studente", studente);
		ctx.setVariable("appelli", appelli);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	private boolean studentBelongsToScaglione(int idScaglione, Studente studente) throws SQLException {
		IscrizioniDAO iscrizionidao = new IscrizioniDAO(connection);
		return iscrizionidao.isStudenteInScaglione(idScaglione, studente);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

}
