package TIW_project_JS.Utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WebSender
{
	HttpServletResponse response;
	Gson gson;
	
	public WebSender()
	{
		this.gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
	}
	
	public void sendObject(HttpServletResponse response, Object object) throws IOException
	{
		this.response = response;
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		String json = gson.toJson(object);
		response.getWriter().write(json);
	}
	
	public void sendString(HttpServletResponse response, String string) throws IOException
	{
		this.response = response;
		
		response.getWriter().write(string);
	}
}
