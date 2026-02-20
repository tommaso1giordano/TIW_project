package TIW_project_JS.Beans;

import java.util.Date;

public class Verbale
{
	private int id;
	private int id_appello;
	private String nome_insegnamento;
	private Date datetime;
	
	public Verbale(int id, int id_appello, String nome_insegnamento, Date datetime)
	{
		this.id = id;
		this.id_appello = id_appello;
		this.nome_insegnamento = nome_insegnamento;
		this.datetime = datetime;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getId_appello()
	{
		return id_appello;
	}
	
	public String getNome_insegnamento()
	{
		return nome_insegnamento;
	}
	
	public Date getDatetime()
	{
		return datetime;
	}
}
