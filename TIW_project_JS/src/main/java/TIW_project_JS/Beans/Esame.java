package TIW_project_JS.Beans;

public class Esame
{
	private int id_appello;
	private int matricola_studente;
	private String nome;
	private String cognome;
	private String email;
	private String corso_di_laurea;
	private String voto;
	private String desc_esito;


	public Esame(int id_appello, int matricola_studente, String nome, String cognome,
			String email, String corso_di_laurea, String desc_esito, String voto)
	{
		this.id_appello = id_appello;
		this.matricola_studente = matricola_studente;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.corso_di_laurea = corso_di_laurea;
		this.desc_esito = desc_esito;
		this.voto = voto;
	}
	
	public Esame(int id_appello, int matricola_studente, String desc_esito, String voto)
	{
		this.id_appello = id_appello;
		this.matricola_studente = matricola_studente;
		this.nome = null;
		this.cognome = null;
		this.email = null;
		this.corso_di_laurea = null;
		this.desc_esito = desc_esito;
		this.voto = voto;
	}
	
	public int getId_appello()
	{
		return id_appello;
	}
	
	public int getMatricola_studente()
	{
		return matricola_studente;
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
	
	public String getCorso_di_laurea()
	{
		return corso_di_laurea;
	}
	
	public String getDesc_esito()
	{
		return desc_esito;
	}
	
	public String getVoto()
	{
		return voto;
	}
}
