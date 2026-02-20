package TIW_project.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterUtils {
    public static Integer getIntParameter(HttpServletRequest request, HttpServletResponse response, String paramName) throws IOException {
        String paramValue = request.getParameter(paramName);

        if (paramValue == null || paramValue.isEmpty()) {
        	response.sendRedirect("Error?errorType=data_missing");
			return null;
        }

        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
        	response.sendRedirect("Error?errorType=not_a_number");
            return null;
        }
    }
    
    public static String getStringParameter(HttpServletRequest request, HttpServletResponse response, String paramName) throws IOException {
        String paramValue = request.getParameter(paramName);

        if (paramValue == null || paramValue.isEmpty()) {
        	response.sendRedirect("Error?errorType=data_missing");
			return null;
        }

        return paramValue;
    }
}