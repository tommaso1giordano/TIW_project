package TIW_project_JS.Utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ParameterUtils
{	
    public static Integer getIntParameter(HttpServletRequest request, HttpServletResponse response, String paramName) throws IOException
    {
        String paramValue = request.getParameter(paramName);

        if (paramValue == null || paramValue.isEmpty()) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametro mancante!");
			return null;
        }

        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Il parametro dovrebbe essere un numero!");
            return null;
        }
    }
    
    public static String getStringParameter(HttpServletRequest request, HttpServletResponse response, String paramName) throws IOException {
        String paramValue = request.getParameter(paramName);

        if (paramValue == null || paramValue.isEmpty()) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	response.getWriter().println("Parametro mancante!");
			return null;
        }

        return paramValue;
    }
}