package TIW_project_JS.Beans;

public class Iscritto
{
	private int matricola;
	private String nominativo;
	private String email;
	private String corsoDiLaurea;
	private String voto;
	private String statoDiValutazione;
	
	public Iscritto(int matricola, String nominativo, String email, String corsoDiLaurea, String voto, String statoDiValutazione)
	{
		this.matricola = matricola;
		this.nominativo = nominativo;
		this.email = email;
		this.corsoDiLaurea = corsoDiLaurea;
		this.voto = voto;
		this.statoDiValutazione = statoDiValutazione;
	}
	
	public int getMatricola()
	{
		return matricola;
	}
	
	public String getNominativo()
	{
		return nominativo;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getCorsoDiLaurea()
	{
		return corsoDiLaurea;
	}
	
	public String getVoto()
	{
		return voto;
	}
	
	public String getStatoDiValutazione()
	{
		return statoDiValutazione;
	}
}