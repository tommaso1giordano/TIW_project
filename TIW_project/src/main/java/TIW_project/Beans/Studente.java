package TIW_project.Beans;

public class Studente
{
	private int matricola;
	private String nome;
	private String cognome;
	private String email;
	private int id_corso_laurea;
	
	public Studente(int matricola, String nome, String cognome, String email, int id_corso_laurea)
	{
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.id_corso_laurea = id_corso_laurea;
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
	public int getId_corso_laurea()
	{
		return id_corso_laurea;
	}
}
