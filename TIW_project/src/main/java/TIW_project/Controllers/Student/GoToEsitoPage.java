package TIW_project.Controllers.Student;

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

import TIW_project.Beans.Appello;
import TIW_project.Beans.Docente;
import TIW_project.Beans.Esame;
import TIW_project.Beans.Scaglione;
import TIW_project.Beans.Studente;
import TIW_project.utils.ParameterUtils;
import TIW_project.utils.ConnectionHandler;
import TIW_project.DAO.AppelloDAO;
import TIW_project.DAO.DocenteDAO;
import TIW_project.DAO.EsameDAO;
import TIW_project.DAO.ScaglioneDAO;

@WebServlet("/Esito")
public class GoToEsitoPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public GoToEsitoPage() {
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
				response.sendRedirect(request.getServletContext().getContextPath() + "/index.html");
				return;
			}
			
			esame = esamedao.getEsame(matricola, idAppello);
			appello = appellodao.getAppelloById(idAppello);
			scaglione = scaglionedao.getScaglioneById(appello.getId_scaglione());
			docente = docentedao.getDocenteByMatricola(scaglione.getMatricola_docente());
		}
		catch (SQLException e)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("esame", esame);
		ctx.setVariable("appello", appello);
		ctx.setVariable("scaglione", scaglione);
		ctx.setVariable("docente", docente);
		ctx.setVariable("studente", studente);
		ctx.setVariable("votoPubblicato", !esame.getDesc_esito().equals("Non inserito") && !esame.getDesc_esito().equals("Inserito"));
		ctx.setVariable("votoRifiutabile", votoRifiutabile(esame));
		ctx.setVariable("votoRifiutato", esame.getDesc_esito().equals("Rifiutato"));
		templateEngine.process("/WEB-INF/EsitoPage.html", ctx, response.getWriter());
	}
	
	private boolean votoRifiutabile(Esame esame) {
		if(!esame.getDesc_esito().equals("Pubblicato"))
			return false;
		
		switch(esame.getVoto()) {
			case "18": return true;
			case "19": return true;
			case "20": return true;
			case "21": return true;
			case "22": return true;
			case "23": return true;
			case "24": return true;
			case "25": return true;
			case "26": return true;
			case "27": return true;
			case "28": return true;
			case "29": return true;
			case "30": return true;
			case "30 e lode": return true;
		}
		return false;
	}

	private boolean studentBelongsToAppello(int idAppello, Studente studente) throws SQLException {
		EsameDAO esamidao = new EsameDAO(connection);
		return esamidao.studentBelongsToAppello(idAppello, studente);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}