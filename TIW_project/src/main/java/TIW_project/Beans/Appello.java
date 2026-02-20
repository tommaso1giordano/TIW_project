package TIW_project.Beans;

import java.sql.Date;

public class Appello
{
	private int id;
	private int id_scaglione;
	private Date date; 
	
	public Appello(int id, int id_scaglione, Date date)
	{
		this.id = id;
		this.id_scaglione = id_scaglione;
		this.date = date;
	}
	
	public int getId()
	{
		return id;
	}
	public int getId_scaglione()
	{
		return id_scaglione;
	}
	public String getDate()
	{
		return date.toString();
	}
}
