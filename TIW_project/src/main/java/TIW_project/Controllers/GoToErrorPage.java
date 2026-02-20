package TIW_project.Controllers;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet("/Error")
public class GoToErrorPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	public void init() throws ServletException
	{
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String errorType = request.getParameter("errorType");
		String errorMessage;
		
		//generic message if errorType is not present or is not recognized
		errorMessage = "Si è verificato un errore!";
		
		//custom errorMessage based on errorType
		if(errorType != null)
		{
			switch(errorType) {
				case("data_missing"):
					errorMessage = "Parametro mancante!";
					break;
				case("not_a_number"):
					errorMessage = "Il parametro dovrebbe essere un numero!";
					break;
				case("grade_not_valid"):
					errorMessage = "Voto inserito non valido!";
					break;
				case("sql"):
					errorMessage = "Si è verificato un errore con la connessione al database!";
					break;
				case("cannot_be_refused"):
					errorMessage = "Il voto non si può rifiutare!";
					break;
				case("already_published"):
					errorMessage = "Il voto è già stato pubblicato!";
					break;
				case("forbidden"):
					errorMessage = "Non hai i permessi per eseguire questa azione!";
					break;
			}
		}
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("errorMessage", errorMessage);
		templateEngine.process("/WEB-INF/ErrorPage.html", ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}