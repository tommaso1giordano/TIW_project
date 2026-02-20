package TIW_project.Beans;

public class Docente
{
	private int matricola;
	private String nome;
	private String cognome;
	private String email;
	
	public Docente (int matricola, String nome, String cognome, String email)
	{
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}

	public int getMatricola()
	{
		return matricola;
	}
	public String getNome()
	{
		return nome;
	}
	public String getCognome()
	{
		return cognome;
	}
	public String getEmail()
	{
		return email;
	}
}