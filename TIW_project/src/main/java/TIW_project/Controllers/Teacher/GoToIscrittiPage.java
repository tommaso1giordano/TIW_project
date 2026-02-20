package TIW_project.Controllers.Teacher;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import TIW_project.Beans.Verbale;
import TIW_project.utils.ConnectionHandler;
import TIW_project.utils.ParameterUtils;
import TIW_project.DAO.AppelloDAO;
import TIW_project.DAO.EsameDAO;
import TIW_project.DAO.VerbaleDAO;

@WebServlet("/IscrittiPage")
public class GoToIscrittiPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;

	public GoToIscrittiPage()
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
	
		String path = "/WEB-INF/IscrittiPage.html"; //path della risorsa HTML fisica
		
		EsameDAO esameDAO;
		VerbaleDAO verbaleDAO;
		
		boolean isEsamiEmpty;
		boolean isVerbaliEmpty;
		
		esameDAO = new EsameDAO(connection);
		verbaleDAO = new VerbaleDAO(connection);
		
		List<Esame> esami = new ArrayList<>();
		List<Verbale> verbali = new ArrayList<>();
		
		Integer idAppello = ParameterUtils.getIntParameter(request, response, "id_appello");
		if(idAppello == null) return;
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
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
			
			esami = esameDAO.getEsamiByAppello(idAppello);
			verbali = verbaleDAO.getVerbaliById_appello(idAppello);
		}
		catch (SQLException e)
		{
			response.sendRedirect("Error?errorType=sql");
			return;
		}
		
		String sortBy = request.getParameter("sortBy");
		String sortOrder = request.getParameter("sortOrder");
		
		if(sortBy == null) sortBy = getDefaultSortBy();
		if(sortOrder == null) sortOrder = getDefaultSortOrder();
		
		switch (sortBy)
		{
			case "matricola_studente" -> esami.sort(Comparator.comparing(Esame::getMatricola_studente));
			case "nominativo" -> esami.sort(Comparator.comparing(Esame::getNominativo));
			case "email" -> esami.sort(Comparator.comparing(Esame::getEmail));
			case "corso_di_laurea" -> esami.sort(Comparator.comparing(Esame::getCorso_di_laurea));
			case "stato_di_valutazione" -> esami.sort(Comparator.comparing(Esame::getDesc_esito));
			
			case "voto" -> esami.sort(new Comparator<Esame>()
			{
	            @Override
	            public int compare(Esame o1, Esame o2)
	            {
	                Map <String, Integer> orderingMap = new HashMap<>();
	                
	                orderingMap.put("<vuoto>", 0);
	                orderingMap.put("Assente", 1);
	                orderingMap.put("Rimandato", 2);
	                orderingMap.put("Riprovato", 3);
	                orderingMap.put("18", 4);
	                orderingMap.put("19", 5);
	                orderingMap.put("20", 6);
	                orderingMap.put("21", 7);
	                orderingMap.put("22", 8);
	                orderingMap.put("23", 9);
	                orderingMap.put("24", 10);
	                orderingMap.put("25", 11);
	                orderingMap.put("26", 12);
	                orderingMap.put("27", 13);
	                orderingMap.put("28", 14);
	                orderingMap.put("29", 15);
	                orderingMap.put("30", 16);
	                orderingMap.put("30 e lode", 17);
	                
	                return orderingMap.get(o1.getVoto()) - orderingMap.get(o2.getVoto());
	            }
	        });
		}
		
		if (sortOrder.equals("desc"))
		{
			Collections.reverse(esami);
		}
		
		if (esami.isEmpty()) isEsamiEmpty = true;
		else isEsamiEmpty = false;
		
		if (verbali.isEmpty())  isVerbaliEmpty = true;
		else isVerbaliEmpty = false;
	
		ctx.setVariable("isVerbaliEmpty", isVerbaliEmpty);
		ctx.setVariable("isEsamiEmpty", isEsamiEmpty);
		ctx.setVariable("sortBy", sortBy);
		ctx.setVariable("sortOrder", sortOrder);
		ctx.setVariable("esami", esami);
		ctx.setVariable("id_appello", idAppello);
		ctx.setVariable("verbali", verbali);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	private String getDefaultSortOrder()
	{
		return "asc";
	}

	private String getDefaultSortBy()
	{
		return "matricola_studente";
	}

	private boolean appelloBelongsToTeacher(int id_appello, Docente docente) throws SQLException
	{
		AppelloDAO appellodao = new AppelloDAO(connection);
		return appellodao.appelloBelongsToTeacher(id_appello, docente);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}
}